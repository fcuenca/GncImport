package gncimport.models;

import gncimport.adaptors.RbcExportParser;
import gncimport.boundaries.TxImportModel;
import gnclib.GncFile;

import java.io.IOException;
import java.util.ArrayList;
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
	private List<TxData> _txList;

	private final String _defaultTargetAccName;

	public LocalFileTxImportModel(String defaultTargetAccName)
	{
		_defaultTargetAccName = defaultTargetAccName;
	}

	@Override
	public List<TxData> fetchTransactionsFrom(String fileName)
	{
		try
		{
			_txList = new RbcExportParser(fileName).getTransactions();

			resetTargetAccountInImportList();

			return _txList;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
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
					return new AccountData(a.getName(), a.getId().getValue());
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
	public List<Account> getAccounts()
	{
		return _gnc.getAccounts();
	}

	@Override
	public void setSourceAccount(AccountData accountData)
	{
		_sourceAccount = accountData;
	}

	@Override
	public AccountData getDefaultTargetHierarchyAccount()
	{
		return new AccountData(_targetHierarcyParent.getName(), _targetHierarcyParent.getId().getValue());
	}

	@Override
	public List<AccountData> getCandidateTargetAccounts()
	{
		ArrayList<AccountData> accounts = new ArrayList<AccountData>();
		List<Account> children = _accTree.get(_targetHierarcyParent.getId().getValue());

		if (children != null)
		{
			for (Account a : children)
			{
				accounts.add(new AccountData(a.getName(), a.getId().getValue()));
			}
		}
		else
		{
			accounts.add(new AccountData(_targetHierarcyParent.getName(), _targetHierarcyParent.getId().getValue()));
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
			_targetAccount = new AccountData(_targetHierarcyParent.getName(), _targetHierarcyParent.getId().getValue());
		}

		if (_txList != null)
		{
			resetTargetAccountInImportList();
		}
	}

	private void resetTargetAccountInImportList()
	{
		AccountData defaultAcc = getDefaultTargetAccount();
		List<AccountData> candidateTargetAccounts = getCandidateTargetAccounts();

		for (TxData txData : _txList)
		{
			if (txData.targetAccount == null)
			{
				txData.targetAccount = defaultAcc;
			}
			else
			{
				AccountData equivalent = findEquivalentInList(txData.targetAccount.getName(), candidateTargetAccounts);

				if (equivalent != null)
				{
					txData.targetAccount = equivalent;
				}
			}
		}
	}

	private AccountData findEquivalentInList(String accName, List<AccountData> candidateTargetAccounts)
	{
		for (AccountData acc : candidateTargetAccounts)
		{
			if (acc.getName().equals(accName))
			{
				return acc;
			}
		}

		return null;
	}
}
