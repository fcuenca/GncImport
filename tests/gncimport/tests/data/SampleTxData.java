package gncimport.tests.data;

import static gncimport.tests.unit.ListUtils.list_of;
import gncimport.transfer.TxData;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SampleTxData
{

	@SuppressWarnings("deprecation")
	public static List<TxData> txDataList()
	{
		return list_of(
				new TxData(new Date(2012 - 1900, 10, 15), new BigDecimal("12"), "Taxi ride"),
				new TxData(new Date(2012 - 1900, 11, 17), new BigDecimal("98"), "Groceries"));
	}

	@SuppressWarnings("deprecation")
	public static List<TxData> txDataListWithSomeAccounts()
	{
		return list_of(
				new TxData(new Date(2012 - 1900, 10, 15), new BigDecimal("12"), "Taxi ride", "Expenses", "id-1"),
				new TxData(new Date(2012 - 1900, 11, 17), new BigDecimal("98"), "Groceries"));
	}

	@SuppressWarnings("deprecation")
	public static List<TxData> txDataListWithAllAccounts()
	{
		return list_of(
				new TxData(new Date(2012 - 1900, 10, 13), new BigDecimal("12"), "Taxi ride", "Expenses", "id-1"),
				new TxData(new Date(2012 - 1900, 10, 14), new BigDecimal("9.95"), "Parking", "Expenses", "id-1"),
				new TxData(new Date(2012 - 1900, 10, 15), new BigDecimal("1.25"), "Coffee", "Expenses", "id-1"),
				new TxData(new Date(2012 - 1900, 10, 17), new BigDecimal("0.78"), "Candy", "Expenses", "id-1"),
				new TxData(new Date(2012 - 1900, 11, 17), new BigDecimal("-100"), "Refund", "Expenses", "id-1"));
	}
}