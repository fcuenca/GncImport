package gncimport.models;

import gncimport.adaptors.RbcExportParser;
import gncimport.boundaries.TxModel;
import gnclib.GncFile;

import java.io.IOException;
import java.util.List;

import org.gnucash.xml.gnc.Account;

public class FakeTxModel implements TxModel
{
	private static final String DEFAULT_SOURCE_ACCOUNT = "Checking Account";
	private static final String DEFAULT_TARGET_ACCOUNT = "Expenses";

	private GncFile _gnc;

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
			for (TxData txData : transactions)
			{
				_gnc.addTransaction(txData.date, txData.description, txData.amount,
						getDefaultSourceAccountId(), txData.targetAccount.getId());
			}

			_gnc.saveTo(fileName);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void openGncFile(String fileName)
	{
		try
		{
			_gnc = new GncFile(fileName);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private String getDefaultSourceAccountId()
	{
		Account account = _gnc.findAccountByName(DEFAULT_SOURCE_ACCOUNT);

		if (account != null)
		{
			return account.getId().getValue();
		}

		throw new RuntimeException("Default Source Account can't be found: " + DEFAULT_SOURCE_ACCOUNT);
	}

	@Override
	public AccountData getDefaultTargetAccount()
	{
		Account account = _gnc.findAccountByName(DEFAULT_TARGET_ACCOUNT);

		if (account != null)
		{
			return new AccountData(account.getName(), account.getId().getValue());
		}

		throw new RuntimeException("Default Target Account can't be found: " + DEFAULT_TARGET_ACCOUNT);
	}

	@Override
	public AccountData getDefaultSourceAccount()
	{
		return new AccountData(DEFAULT_SOURCE_ACCOUNT, "id-niy");
	}

	@Override
	public List<Account> getAccounts()
	{
		return _gnc.getAccounts();
	}
}
