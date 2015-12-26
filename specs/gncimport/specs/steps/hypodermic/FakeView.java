package gncimport.specs.steps.hypodermic;

import gncimport.models.AccountData;
import gncimport.ui.TxView;
import gncimport.ui.swing.TxTableModel;

import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;


public class FakeView implements TxView
{

	public String FileNameToOpen = "";
	public int TxCount = -1;
	public TxTableModel TableModel;
	public List<AccountData> TargetAccounts;
	public String CsvFileLabel;
	public String AccountToSelect;

	@Override
	public void displayTxCount(int count)
	{
		TxCount = count;
	}

	@Override
	public void displayTxData(TxTableModel tableModel, List<AccountData> targetAccounts)
	{
		TableModel = tableModel;
		TargetAccounts = targetAccounts;
	}

	@Override
	public TxTableModel getTxTableModel()
	{
		throw new RuntimeException("NIY");
	}

	@Override
	public void handleException(Exception exception)
	{
		throw new RuntimeException(exception);
	}

	@Override
	public void displaySourceAccount(String accountName)
	{
		throw new RuntimeException("NIY");
	}

	@Override
	public DefaultMutableTreeNode promptForAccount(DefaultMutableTreeNode rootNode)
	{
		Enumeration<TreeNode> e = rootNode.preorderEnumeration();
	    while(e.hasMoreElements())
	    {
	    	TreeNode node = e.nextElement();
	    	if(node.toString().equals(AccountToSelect))
	    	{
	    		return (DefaultMutableTreeNode) node;
	    	}
	        
	    }
	    
	    throw new RuntimeException("couldn't find account in tree!");
	}

	@Override
	public void displayTargetHierarchy(String accountName)
	{
	}

	@Override
	public void updateCandidateTargetAccountList(List<AccountData> accountList)
	{
	}

	@Override
	public String promptForFile(String initialDirectory)
	{
		return FileNameToOpen;
	}

	@Override
	public void updateGncFileLabel(String text)
	{
	}

	@Override
	public void updateCsvFileLabel(String text)
	{
		CsvFileLabel = text;
	}

	@Override
	public NewHierarchyParams promptForNewHierarchy(DefaultMutableTreeNode rootNode)
	{
		throw new RuntimeException("NIY");
	}

	@Override
	public void displayErrorMessage(String message)
	{
		throw new RuntimeException("NIY");
	}

	@Override
	public void selectExpenseAccForTx(AccountData newAcc)
	{		
	}
}
