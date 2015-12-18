package gncimport.ui;

import gncimport.models.AccountData;
import gncimport.models.Month;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public interface TxView
{
	void displayTxCount(int count);

	void displayTxData(TxTableModel tableModel, List<AccountData> targetAccounts);

	TxTableModel getTxTableModel();

	void handleException(Exception exception);

	void displaySourceAccount(String accountName);

	DefaultMutableTreeNode promptForAccount(DefaultMutableTreeNode rootNode);

	void displayTargetHierarchy(String accountName);

	void updateCandidateTargetAccountList(List<AccountData> accountList);

	String promptForFile(String initialDirectory);

	void updateGncFileLabel(String text);

	void updateCsvFileLabel(String text);
	
	public class NewHierarchyParams
	{
		public DefaultMutableTreeNode parentNode;
		public String rootAccName;
		public Month month;
	}

	NewHierarchyParams promptForNewHierarchy(DefaultMutableTreeNode rootNode);

	void displayErrorMessage(String message);

	void selectExpenseAccForTx(AccountData newAcc);
}
