package gncimport.ui;

import gncimport.boundaries.TxImportModel;
import gncimport.boundaries.TxView;
import gncimport.models.AccountData;
import gncimport.models.TxData;

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

			setDefaultTargetAccount(newTransactionData);

			_view.displayTxData(new TxTableModel(newTransactionData), _model.getCandidateTargetAccounts());
			_view.displayTxCount(newTransactionData.size());
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	private void setDefaultTargetAccount(List<TxData> newTransactionData)
	{
		AccountData targetAcc = _model.getDefaultTargetAccount();

		for (TxData txData : newTransactionData)
		{
			txData.targetAccount = targetAcc;
		}
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
			List<Account> accounts = _model.getAccounts();

			AccountTreeBuilder builder = new AccountTreeBuilder();
			for (Account account : accounts)
			{
				builder.addNodeFor(account);
			}

			DefaultMutableTreeNode selectedNode = _view.displayAccountTree(builder.getRoot());

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
}
