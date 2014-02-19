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
	private static final String DEFAULT_TARGET_HIERARCHY = "Enero 2014";
	private static final String DEFAULT_SOURCE_ACCOUNT = "Checking Account";
	private static final String DEFAULT_TARGET_ACCOUNT = "Expenses";

	private GncFile _gnc;
	private AccountData _targetAccount;
	private AccountData _sourceAccount;
	private Map<String, List<Account>> _accTree = new HashMap<String, List<Account>>();
	private Account _targetHierarcyParent;
	private List<TxData> _txList;

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

	private void resetTargetAccountInImportList()
	{
		AccountData targetAcc = getDefaultTargetAccount();

		for (TxData txData : _txList)
		{
			txData.targetAccount = targetAcc;
		}
	}

	@Override
	public void saveTxTo(List<TxData> transactions, String fileName)
	{
		try
		{
			saveToGncFile(fileName, transactions, _sourceAccount.getId());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected void saveToGncFile(String fileName, List<TxData> transactions, String sourceAccId) throws IOException
	{
		for (TxData txData : transactions)
		{
			_gnc.addTransaction(txData.date, txData.description, txData.amount,
					sourceAccId, txData.targetAccount.getId());
		}

		_gnc.saveTo(fileName);
	}

	@Override
	public void openGncFile(String fileName)
	{
		try
		{
			_gnc = new GncFile(fileName);

			initializeTargetAccount(DEFAULT_TARGET_HIERARCHY, DEFAULT_TARGET_ACCOUNT);
			initializeSourceAccount(DEFAULT_SOURCE_ACCOUNT);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void initializeSourceAccount(String accountName)
	{
		_sourceAccount = loadAccount(accountName);
	}

	private void initializeTargetAccount(String hierarchyName, String accountName)
	{
		initializeTargetHierarchy(hierarchyName);
		_targetAccount = loadAccountUnderHierarchy(accountName);
	}

	@Override
	public AccountData getDefaultTargetAccount()
	{
		return _targetAccount;
	}

	@Override
	public AccountData getDefaultSourceAccount()
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

		for (Account a : _accTree.get(_targetHierarcyParent.getId().getValue()))
		{
			accounts.add(new AccountData(a.getName(), a.getId().getValue()));
		}

		return accounts;
	}

	@Override
	public void setTargetAccount(AccountData accountData)
	{
		initializeTargetAccount(accountData.getName(), DEFAULT_TARGET_ACCOUNT);
		if (_txList != null)
		{
			resetTargetAccountInImportList();
		}
	}

	private AccountData loadAccount(String accName)
	{
		Account account = _gnc.findAccountByName(accName);

		if (account != null)
		{
			return new AccountData(account.getName(), account.getId().getValue());
		}

		throw new RuntimeException("Account can't be found: " + accName);
	}

	private AccountData loadAccountUnderHierarchy(String accName)
	{
		Account account = null;
		for (Account a : _accTree.get(_targetHierarcyParent.getId().getValue()))
		{
			if (a.getName().equals(accName))
			{
				account = a;
				break;
			}
		}

		if (account != null)
		{
			return new AccountData(account.getName(), account.getId().getValue());
		}

		throw new RuntimeException("Account can't be found: " + accName);
	}

	private void initializeTargetHierarchy(String parentName)
	{
		for (Account a : _gnc.getAccounts())
		{
			if (a.getName().equals(parentName))
			{
				_targetHierarcyParent = a;
			}

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
}
