package gncimport.boundaries;

import gncimport.models.AccountData;
import gncimport.ui.TxTableModel;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public interface TxView
{
	void displayTxCount(int count);

	void displayTxData(TxTableModel tableModel, List<AccountData> targetAccounts);

	TxTableModel getTxTableModel();

	void handleException(Exception exception);

	void displaySourceAccount(String accountName);

	DefaultMutableTreeNode displayAccountTree(DefaultMutableTreeNode rootNode);
}
