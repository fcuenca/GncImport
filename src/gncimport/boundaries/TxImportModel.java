package gncimport.boundaries;

import gncimport.models.AccountData;
import gncimport.models.TxData;

import java.util.List;

import org.gnucash.xml.gnc.Account;

public interface TxImportModel
{
	List<TxData> fetchTransactionsFrom(String fileName);

	void openGncFile(String fileName);

	AccountData getDefaultTargetAccount();

	AccountData getDefaultSourceAccount();

	List<Account> getAccounts();

	void setSourceAccount(AccountData accountData);

	void saveTxTo(List<TxData> transactions, String fileName);

	List<AccountData> getCandidateTargetAccounts();
}
