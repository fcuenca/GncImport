package gncimport.models;

import gncimport.boundaries.TxModel;

public class FakeTxModel implements TxModel
{
	@Override
	public int getTxCount()
	{
		return 0;
	}

}
