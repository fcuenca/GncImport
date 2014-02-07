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
		List<TxData> txData = _model.fetchTransactionsFrom(fileName);

		_view.displayTxData(new TxTableModel(txData));
		_view.displayTxCount(txData.size());
	}

	public void onSaveToGncFile(String fileName)
	{
		List<TxData> txData = _view.getTxTableModel().getTransactions();

		_model.saveTxTo(txData, fileName);
	}
}
