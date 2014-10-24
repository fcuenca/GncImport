package gncimport.ui;

import gncimport.models.AccountData;

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

	void displayTargetHierarchy(String accountName);

	void updateCandidateTargetAccountList(List<AccountData> accountList);

	String promptForFile(String initialDirectory);

	void updateGncFileLabel(String text);

	void updateCsvFileLabel(String text);
}
