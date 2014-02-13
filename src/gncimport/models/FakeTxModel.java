package gncimport.models;

import gncimport.adaptors.RbcExportParser;
import gncimport.boundaries.TxModel;
import gnclib.GncFile;

import java.io.IOException;
import java.util.List;

public class FakeTxModel implements TxModel
{
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
			throw new RuntimeException(e); // TODO: bubble up and handle in
											// presenter
		}
	}
}
