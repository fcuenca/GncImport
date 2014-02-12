package gncimport.tests.unit;

import static gncimport.tests.unit.ListUtils.list_of;
import gncimport.models.TxData;

import java.math.BigDecimal;
import java.util.List;

public class SampleTxData
{
	private static List<TxData> _sampleData = list_of(
			new TxData("Nov 15, 2012", new BigDecimal("12"), "Taxi ride"),
			new TxData("Dec 17, 2012", new BigDecimal("98"), "Groceries"));;

	public static List<TxData> txDataList()
	{
		return _sampleData;
	}

	public static int dataListCount()
	{
		return _sampleData.size();
	}
}