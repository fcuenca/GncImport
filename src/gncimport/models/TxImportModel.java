package gncimport.models;

import gncimport.transfer.AccountData;
import gncimport.transfer.Month;
import gncimport.transfer.MonthlyAccountParam;
import gncimport.transfer.TxData;

import java.util.Date;
import java.util.List;

public interface TxImportModel
{
	List<TxData> fetchTransactionsFrom(String fileName);

	void openGncFile(String fileName);

	AccountData getDefaultTargetAccount();

	AccountData getSourceAccount();

	List<AccountData> getAccounts();

	void setSourceAccount(AccountData accountData);

	void saveTxTo(List<TxData> transactions, String fileName);

	List<AccountData> getCandidateTargetAccounts();

	AccountData getDefaultTargetHierarchyAccount();

	void setTargetHierarchy(AccountData accountData);

	List<TxData> filterTxList(Date fromDate, Date toDate);

	void createNewAccountHierarchy(AccountData parentAccount, String hierarchyRoot, Month month, 
			List<MonthlyAccountParam> subAccList, String fileNameToSave);
}
