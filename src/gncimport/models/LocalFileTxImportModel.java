package gncimport.models;

import gncimport.adaptors.RbcExportParser;
import gncimport.boundaries.TxImportModel;
import gnclib.GncFile;

import java.io.IOException;
import java.util.List;

import org.gnucash.xml.gnc.Account;

public class LocalFileTxImportModel implements TxImportModel
{
	private static final String DEFAULT_SOURCE_ACCOUNT = "Checking Account";
	private static final String DEFAULT_TARGET_ACCOUNT = "Expenses";

	private GncFile _gnc;
	private AccountData _defaultTargetAccount;
	private AccountData _sourceAccount;

	@Override
	public List<TxData> fetchTransactionsFrom(String fileName)
	{
		try
		{
			return new RbcExportParser(fileName).getTransactions();
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
			_defaultTargetAccount = loadAccount(DEFAULT_TARGET_ACCOUNT);
			_sourceAccount = loadAccount(DEFAULT_SOURCE_ACCOUNT);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public AccountData getDefaultTargetAccount()
	{
		return _defaultTargetAccount;
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

	private AccountData loadAccount(String accName)
	{
		Account account = _gnc.findAccountByName(accName);

		if (account != null)
		{
			return new AccountData(account.getName(), account.getId().getValue());
		}

		throw new RuntimeException("Account can't be found: " + DEFAULT_TARGET_ACCOUNT);
	}
}
