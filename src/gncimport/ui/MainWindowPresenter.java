package gncimport.ui;

import gncimport.models.AccountData;
import gncimport.models.TxData;
import gncimport.models.TxImportModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.gnucash.xml.gnc.Account;

public class MainWindowPresenter implements MainWindowRenderer
{
	public static final AccountData OTHER_ACC_PLACEHOLDER = new AccountData("<< OTHER >>", "-1");

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
		candidates.add(OTHER_ACC_PLACEHOLDER);

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
			AccountData selectedAccount = selectAccountFromTree();

			if (selectedAccount != null)
			{
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
	public void onSelectTargetHierarchy()
	{
		try
		{
			AccountData selectedAccount = selectAccountFromTree();
			if (selectedAccount != null)
			{
				_model.setTargetHierarchy(selectedAccount);
				_view.displayTargetHierarchy(selectedAccount.getName());
				_view.updateCandidateTargetAccountList(buildTargetAccountList());
			}
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	@Override
	public AccountData onTargetAccountSelected(AccountData newAcc, AccountData originalAcc)
	{
		if (!newAcc.equals(OTHER_ACC_PLACEHOLDER))
		{
			return newAcc;
		}

		try
		{
			final AccountData selectedAcc = selectAccountFromTree();
			if (selectedAcc != null)
			{
				return selectedAcc;
			}
			else
			{
				return originalAcc;
			}
		}
		catch (Exception e)
		{
			_view.handleException(e);
			return originalAcc;
		}
	}

	private AccountData selectAccountFromTree()
	{
		DefaultMutableTreeNode accountRoot = getAccountTree();
		DefaultMutableTreeNode selectedNode = _view.displayAccountTree(accountRoot);

		if (selectedNode != null)
		{
			AccountData selectedAccount = (AccountData) selectedNode.getUserObject();
			return selectedAccount;
		}

		return null;
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

	@SuppressWarnings("deprecation")
	@Override
	public void onFilterTxList(Date fromDate, Date toDate)
	{
		Date lowerBound = fromDate != null ? fromDate : new Date(Long.MIN_VALUE);

		Date upperBound = toDate;
		if (upperBound != null)
		{
			upperBound = (Date) toDate.clone();
			upperBound.setHours(23);
			upperBound.setMinutes(59);
			upperBound.setSeconds(59);
		}
		else
		{
			upperBound = new Date(Long.MAX_VALUE);
		}

		List<TxData> filteredTxList = _model.filterTxList(lowerBound, upperBound);

		_view.displayTxData(new TxTableModel(filteredTxList), buildTargetAccountList());
		_view.displayTxCount(filteredTxList.size());
	}

}
