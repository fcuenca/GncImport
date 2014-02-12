package gncimport.tests.unit;

import static gncimport.tests.unit.ListUtils.list_of;
import gncimport.models.TxData;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SampleTxData
{
	@SuppressWarnings("deprecation")
	private static List<TxData> _sampleData = list_of(
			new TxData(new Date(2012 - 1900, 10, 15), new BigDecimal("12"), "Taxi ride"),
			new TxData(new Date(2012 - 1900, 11, 17), new BigDecimal("98"), "Groceries"));;

	public static List<TxData> txDataList()
	{
		return _sampleData;
	}

	public static int dataListCount()
	{
		return _sampleData.size();
	}
}