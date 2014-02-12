package gncimport.models;

import gncimport.adaptors.RbcExportParser;
import gncimport.boundaries.TxModel;
import gnclib.GncFile;

import java.io.IOException;
import java.util.List;

public class FakeTxModel implements TxModel
{
	private static final String TARGET_ACCOUNT_ID = "e31486ad3b2c6cdedccf135d13538b29";
	private static final String SOURCE_ACCOUNT_ID = "64833494284bad5fb390e84d38c65a54";

	@Override
	public List<TxData> fetchTransactionsFrom(String fileName)
	{
		try
		{
			return new RbcExportParser(fileName).getTransactions();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e); // TODO: this needs to bubble up and
											// be handled at the presenter
		}
	}

	@Override
	public void saveTxTo(List<TxData> transactions, String fileName)
	{
		try
		{
			GncFile gnc = new GncFile(fileName);

			for (TxData txData : transactions)
			{
				gnc.addTransaction(txData.date, txData.description, txData.amount,
						SOURCE_ACCOUNT_ID, TARGET_ACCOUNT_ID);
			}

			gnc.saveTo("/tmp/checkbook-new.xml");
		}
		catch (IOException e)
		{
			throw new RuntimeException(e); // TODO: bubble up and handle in
											// presenter
		}
	}
}
