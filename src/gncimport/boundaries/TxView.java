package gncimport.boundaries;

import gncimport.ui.TxTableModel;

import java.util.Map;

public interface TxView
{

	void displayTxCount(int count);

	void displayTxData(TxTableModel tableModel);

	TxTableModel getTxTableModel();

	String getSourceAccountId();

	void handleException(Exception exception);

	void displayAccounts(Map<String, String> accountList, String selectedAccountId);

}
