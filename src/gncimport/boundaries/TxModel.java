package gncimport.boundaries;

import gncimport.models.TxData;

import java.util.List;
import java.util.Map;

public interface TxModel
{
	void openGncFile(String fileName);

	List<TxData> fetchTransactionsFrom(String fileName);

	void saveTxTo(List<TxData> transactions, String sourceAccountId, String fileName);

	Map<String, String> getAccounts();

	String getDefaultSourceAccountId();

	String getDefaultTargetAccountId();
}
