package gncimport.boundaries;

import gncimport.models.AccountData;
import gncimport.models.TxData;

import java.util.List;

import org.gnucash.xml.gnc.Account;

public interface TxModel
{
	void openGncFile(String fileName);

	List<TxData> fetchTransactionsFrom(String fileName);

	void saveTxTo(List<TxData> transactions, String fileName);

	AccountData getDefaultTargetAccount();

	AccountData getDefaultSourceAccount();

	List<Account> getAccounts();
}
