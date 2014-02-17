package gncimport.boundaries;

import gncimport.ui.TxTableModel;

public interface TxView
{
	void displayTxCount(int count);

	void displayTxData(TxTableModel tableModel);

	TxTableModel getTxTableModel();

	void handleException(Exception exception);

	void displaySourceAccount(String accountName);
}
