package gncimport.models;

import gncimport.boundaries.TxModel;

import java.util.ArrayList;
import java.util.List;

public class FakeTxModel implements TxModel
{
	@Override
	public List<TxData> fetchTransactions()
	{
		return new ArrayList<TxData>();
	}

}
