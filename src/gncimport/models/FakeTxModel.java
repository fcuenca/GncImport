package gncimport.models;

import static gncimport.tests.unit.ListUtils.list_of;
import gncimport.boundaries.TxModel;

import java.util.List;

public class FakeTxModel implements TxModel
{
	@Override
	public List<TxData> fetchTransactions()
	{
		return list_of(
				new TxData("Nov 15, 2012", 12, "Taxi ride"),
				new TxData("Dec 17, 2012", 98, "Groceries"));
	}

}
