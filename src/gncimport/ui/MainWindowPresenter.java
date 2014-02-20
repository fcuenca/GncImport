package gncimport.ui;

import gncimport.boundaries.TxImportModel;
import gncimport.boundaries.TxView;
import gncimport.models.AccountData;
import gncimport.models.TxData;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.gnucash.xml.gnc.Account;

public class MainWindowPresenter implements MainWindowRenderer
{
	private final TxImportModel _model;
	private final TxView _view;

	public MainWindowPresenter(TxImportModel model, TxView view)
	{
		this._model = model;
		this._view = view;
	}

	@Override
	public void onReadFromCsvFile(String fileName)
	{
		List<TxData> newTransactionData;
		try
		{
			newTransactionData = _model.fetchTransactionsFrom(fileName);

			_view.displayTxData(new TxTableModel(newTransactionData), buildTargetAccountList());
			_view.displayTxCount(newTransactionData.size());
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	private List<AccountData> buildTargetAccountList()
	{
		List<AccountData> candidates = new ArrayList<AccountData>(_model.getCandidateTargetAccounts());
		candidates.add(new AccountData("<< OTHER >>", "-1"));

		return candidates;
	}

	@Override
	public void onSaveToGncFile(String fileName)
	{
		try
		{
			List<TxData> txData = _view.getTxTableModel().getTransactions();

			_model.saveTxTo(txData, fileName);
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	@Override
	public void onLoadGncFile(String fileName)
	{
		try
		{
			_model.openGncFile(fileName);
			_view.displaySourceAccount(_model.getDefaultSourceAccount().getName());
			_view.displayTargetHierarchy(_model.getDefaultTargetHierarchyAccount().getName());
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	@Override
	public void onSelectSourceAccount()
	{
		try
		{
			DefaultMutableTreeNode accountRoot = getAccountTree();

			DefaultMutableTreeNode selectedNode = _view.displayAccountTree(accountRoot);

			if (selectedNode != null)
			{
				AccountData selectedAccount = (AccountData) selectedNode.getUserObject();

				_model.setSourceAccount(selectedAccount);
				_view.displaySourceAccount(selectedAccount.getName());
			}
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	@Override
	public void onSelectTargetAccount()
	{
		try
		{
			DefaultMutableTreeNode accountRoot = getAccountTree();

			DefaultMutableTreeNode selectedNode = _view.displayAccountTree(accountRoot);

			if (selectedNode != null)
			{
				AccountData selectedAccount = (AccountData) selectedNode.getUserObject();

				_model.setTargetAccount(selectedAccount);
				_view.displayTargetHierarchy(selectedAccount.getName());
				_view.updateCandidateTargetAccountList(buildTargetAccountList());
			}
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	private DefaultMutableTreeNode getAccountTree()
	{
		List<Account> accounts = _model.getAccounts();

		AccountTreeBuilder builder = new AccountTreeBuilder();
		for (Account account : accounts)
		{
			builder.addNodeFor(account);
		}

		DefaultMutableTreeNode accountRoot = builder.getRoot();
		return accountRoot;
	}
}
