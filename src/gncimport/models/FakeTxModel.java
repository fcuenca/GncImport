package gncimport.models;

import gncimport.adaptors.RbcExportParser;
import gncimport.boundaries.TxModel;
import gnclib.GncFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FakeTxModel implements TxModel
{
	private static final String DEFAULT_SOURCE_ACCOUNT_ID = "64833494284bad5fb390e84d38c65a54";
	private static final String DEFAULT_TARGET_ACCOUNT_ID = "e31486ad3b2c6cdedccf135d13538b29";

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
	public void saveTxTo(List<TxData> transactions, String sourceAccountId, String fileName)
	{
		try
		{
			GncFile gnc = new GncFile(fileName);

			for (TxData txData : transactions)
			{
				gnc.addTransaction(txData.date, txData.description, txData.amount,
						sourceAccountId, txData.targetAccoundId);
			}

			gnc.saveTo("/tmp/checkbook-new.xml");
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void openGncFile(String fileName)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, String> getAccounts()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultSourceAccountId()
	{
		return DEFAULT_SOURCE_ACCOUNT_ID;
	}

	@Override
	public String getDefaultTargetAccountId()
	{
		return DEFAULT_TARGET_ACCOUNT_ID;
	}
}
