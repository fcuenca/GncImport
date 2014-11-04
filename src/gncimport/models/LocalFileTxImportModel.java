package gncimport.models;

import gncimport.adaptors.RbcExportParser;
import gncimport.utils.ProgrammerError;
import gnclib.GncFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gnucash.xml.gnc.Account;

public class LocalFileTxImportModel implements TxImportModel
{
	protected GncFile _gnc;
	private AccountData _targetAccount;
	private AccountData _sourceAccount;
	private Map<String, List<Account>> _accTree = new HashMap<String, List<Account>>();
	private Account _targetHierarcyParent;
	private List<TxData> _txListOriginal;

	private final String _defaultTargetAccName;
	
	private TxMatcher _txMatcher = new TxMatcher()
	{
		@Override
		public String findAccountOverride(String txDescription)
		{
			return null;
		}

		@Override
		public boolean isToBeIgnored(String txDescription)
		{
			return false;
		}
	};

	public LocalFileTxImportModel(String defaultTargetAccName)
	{
		_defaultTargetAccName = defaultTargetAccName;
	}
	
	public void setTransactionMatchingRules(TxMatcher matcher)
	{
		this._txMatcher = matcher;		
	}

	@Override
	public List<TxData> fetchTransactionsFrom(String fileName)
	{
		try
		{
			_txListOriginal = new RbcExportParser(fileName).getTransactions();
			
			for (TxData txData : _txListOriginal)
			{
				applyRules(txData);
			}

			return _txListOriginal;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	//TODO: this is a separate class wanting to emerge. 
	private void applyRules(TxData txData)
	{
		applyRule_overrideAcc(txData); 
		applyRule_setIgnore(txData);
	}

	private void applyRule_setIgnore(TxData txData)
	{
		txData.doNotImport = _txMatcher.isToBeIgnored(txData.description);
	}

	private void applyRule_overrideAcc(TxData txData)
	{
		//This is the problem: it depends on the acc Tree
		String overrideAccName = _txMatcher.findAccountOverride(txData.description);
		txData.targetAccount = overrideAccName != null ? findAccountUnderTargetHierarchy(overrideAccName) : getDefaultTargetAccount();
	}

	@Override
	public void saveTxTo(List<TxData> transactions, String fileName)
	{
		try
		{
			addNewTransactions(transactions, _sourceAccount.getId());
			saveToGncFile(fileName);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected void addNewTransactions(List<TxData> transactions, String sourceAccId)
	{
		for (TxData txData : transactions)
		{
			if (txData.doNotImport == false)
			{
				_gnc.addTransaction(txData.date, txData.description, txData.amount,
						sourceAccId, txData.targetAccount.getId());
			}
		}
	}

	protected void saveToGncFile(String fileName) throws IOException
	{
		_gnc.saveTo(fileName);
	}

	@Override
	public void openGncFile(String fileName)
	{
		try
		{
			_gnc = new GncFile(fileName);

			initializeAccountTree();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void initializeAccountTree()
	{
		_accTree = new HashMap<String, List<Account>>();

		for (Account a : _gnc.getAccounts())
		{
			String parentId = a.getParent() != null ? a.getParent().getValue() : null;
			List<Account> children = _accTree.get(parentId);
			if (children == null)
			{
				children = new ArrayList<Account>();
				_accTree.put(parentId, children);
			}
			children.add(a);
		}
	}

	private AccountData findAccountUnderTargetHierarchy(String accName)
	{
		List<Account> children = _accTree.get(_targetHierarcyParent.getId().getValue());

		if (children != null)
		{
			for (Account a : children)
			{
				if (a.getName().equals(accName))
				{
					return AccountData.fromAccount(a); 
				}
			}
		}
		return null;
	}

	@Override
	public AccountData getDefaultTargetAccount()
	{
		return _targetAccount;
	}

	@Override
	public AccountData getSourceAccount()
	{
		return _sourceAccount;
	}

	@Override
	public List<AccountData> getAccounts()
	{
		List<Account> accounts = _gnc.getAccounts();

		List<AccountData> accData = new ArrayList<AccountData>();
		for (Account account : accounts)
		{
			accData.add(AccountData.fromAccount(account));
		}

		return accData;
	}

	@Override
	public void setSourceAccount(AccountData accountData)
	{
		_sourceAccount = accountData;
	}

	@Override
	public AccountData getDefaultTargetHierarchyAccount()
	{
		return AccountData.fromAccount(_targetHierarcyParent); 
	}

	@Override
	public List<AccountData> getCandidateTargetAccounts()
	{
		ArrayList<AccountData> accounts = new ArrayList<AccountData>();

		if (_targetHierarcyParent != null)
		{
			List<Account> children = _accTree.get(_targetHierarcyParent.getId().getValue());

			if (children != null)
			{
				for (Account a : children)
				{
					accounts.add(AccountData.fromAccount(a));
				}
			}
			else
			{
				accounts.add(AccountData.fromAccount(_targetHierarcyParent));
			}
		}
		return accounts;
	}

	@Override
	public void setTargetHierarchy(AccountData accountData)
	{
		_targetHierarcyParent = _gnc.findAccountByName(accountData.getName());

		if (_targetHierarcyParent == null)
		{
			throw new IllegalArgumentException("Target hierarchy not found: " + accountData.getName());
		}

		_targetAccount = findAccountUnderTargetHierarchy(_defaultTargetAccName);

		if (_targetAccount == null)
		{
			_targetAccount = AccountData.fromAccount(_targetHierarcyParent); 
		}

		if (_txListOriginal != null)
		{
			resetTargetAccountInImportList();
		}
	}

	private void resetTargetAccountInImportList()
	{
		for (TxData txData : _txListOriginal)
		{
			AccountData equivalent = findAccountUnderTargetHierarchy(txData.targetAccount.getName());

			if (equivalent != null)
			{
				txData.targetAccount = equivalent;
			}
		}
	}

	@Override
	public List<TxData> filterTxList(Date fromDate, Date toDate)
	{
		if (_txListOriginal == null)
		{
			throw new ProgrammerError("fetch hasn't been called yet!");
		}

		ArrayList<TxData> filteredList = new ArrayList<TxData>();

		for (TxData tx : _txListOriginal)
		{
			if (tx.date.compareTo(fromDate) >= 0 && tx.date.compareTo(toDate) <= 0)
			{
				filteredList.add(tx);
			}
		}

		return filteredList;
	}

	@Override
	public void createNewAccountHierarchy(AccountData parentAccount, String rootAccountName, Month month, List<MonthlyAccountParam> subAccList, String fileToSave)
	{
		try
		{			
			Account parent = _gnc.findAccountById(parentAccount.getId());
									
			String parentCode = parent.getCode();
			
			String code = generateSubAccountCode(parentCode, month, 0);
			
			Account root = _gnc.addSubAccount(rootAccountName, code, parent);
			
			for (MonthlyAccountParam p : subAccList)
			{
				_gnc.addSubAccount(p.accName, generateSubAccountCode(parentCode, month, p.sequenceNo), 	root);
			}
			
			saveToGncFile(fileToSave);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private String generateSubAccountCode(String parentCode, Month month, int sequenceNo)
	{
		return parentCode.replaceAll("4(\\d\\d\\d\\d)(\\d\\d)00", 
				"4$1" +
				month.toNumericString() +
				String.format("%02d", sequenceNo));
	}
}
