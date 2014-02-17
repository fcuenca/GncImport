package gncimport.boundaries;

import gncimport.ui.TxTableModel;

import javax.swing.tree.DefaultMutableTreeNode;

public interface TxView
{
	void displayTxCount(int count);

	void displayTxData(TxTableModel tableModel);

	TxTableModel getTxTableModel();

	void handleException(Exception exception);

	void displaySourceAccount(String accountName);

	void displayAccountTree(DefaultMutableTreeNode rootNode);
}
