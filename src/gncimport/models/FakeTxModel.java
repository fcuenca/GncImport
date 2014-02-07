package gncimport.models;

import gncimport.adaptors.RbcExportParser;
import gncimport.boundaries.TxModel;

import java.util.ArrayList;
import java.util.List;

public class FakeTxModel implements TxModel
{
	@Override
	public List<TxData> fetchTransactionsFrom(String fileName)
	{
		List<TxData> result = new ArrayList<TxData>();
		try
		{
			RbcExportParser parser = new RbcExportParser(fileName);
			return parser.getTransactions();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}

}
