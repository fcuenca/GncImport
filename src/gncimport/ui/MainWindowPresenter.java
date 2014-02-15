package gncimport.ui;

import gncimport.boundaries.TxModel;
import gncimport.boundaries.TxView;
import gncimport.models.TxData;

import java.util.List;

public class MainWindowPresenter implements MainWindowRenderer
{
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
			String targetAccId = _model.getDefaultTargetAccountId();

			for (TxData txData : newTransactionData)
			{
				txData.targetAccoundId = targetAccId;
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
			_view.displayAccounts(_model.getAccounts(), _model.getDefaultSourceAccountId());
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}
}
