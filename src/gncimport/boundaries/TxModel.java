package gncimport.boundaries;

import gncimport.models.TxData;

import java.util.List;

public interface TxModel
{
	List<TxData> fetchTransactionsFrom(String fileName);

	void saveTxTo(List<TxData> transactions, String sourceAccountId, String fileName);
}
