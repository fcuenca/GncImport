package gncimport.specs.steps.hypodermic;

import gncimport.models.AccountData;
import gncimport.ui.TxTableModel;
import gncimport.ui.TxView;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;


public class FakeView implements TxView
{

	public String FileName = "";
	public int TxCount = -1;
	public TxTableModel TableModel;
	public List<AccountData> TargetAccounts;
	public String CsvFileLabel;

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
		throw new RuntimeException("NIY");
	}

	@Override
	public void displayTargetHierarchy(String accountName)
	{
		throw new RuntimeException("NIY");
	}

	@Override
	public void updateCandidateTargetAccountList(List<AccountData> accountList)
	{
		throw new RuntimeException("NIY");
	}

	@Override
	public String promptForFile(String initialDirectory)
	{
		return FileName;
	}

	@Override
	public void updateGncFileLabel(String text)
	{
		throw new RuntimeException("NIY");
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
}
