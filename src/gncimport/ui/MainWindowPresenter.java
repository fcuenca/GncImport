package gncimport.ui;

import gncimport.boundaries.TxModel;
import gncimport.boundaries.TxView;
import gncimport.models.TxData;

import java.util.List;

public class MainWindowPresenter implements MainWindowRenderer
{
	private static final String DEFAULT_SOURCE_ACCOUNT_ID = "64833494284bad5fb390e84d38c65a54";
	private static final String DEFAULT_TARGET_ACCOUNT_ID = "e31486ad3b2c6cdedccf135d13538b29";

	private final TxModel _model;
	private final TxView _view;

	public MainWindowPresenter(TxModel model, TxView view)
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

			for (TxData txData : newTransactionData)
			{
				txData.targetAccoundId = DEFAULT_TARGET_ACCOUNT_ID;
			}

			_view.displayTxData(new TxTableModel(newTransactionData));
			_view.displayTxCount(newTransactionData.size());
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	@Override
	public void onSaveToGncFile(String fileName)
	{
		try
		{
			List<TxData> txData = _view.getTxTableModel().getTransactions();

			_model.saveTxTo(txData, _view.getSourceAccountId(), fileName);
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
			_view.displayAccounts(_model.getAccounts(), DEFAULT_SOURCE_ACCOUNT_ID);
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}
}
