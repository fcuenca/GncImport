package gncimport.boundaries;

import gncimport.models.TxData;

import java.util.List;

public interface TxModel
{
	void openGncFile(String fileName);

	List<TxData> fetchTransactionsFrom(String fileName);

	void saveTxTo(List<TxData> transactions, String fileName);

	String getDefaultTargetAccountId();

	String getDefaultSourceAccountName();
}
