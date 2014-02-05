package gncimport.presenters;

import gncimport.boundaries.TxModel;
import gncimport.boundaries.TxView;

public class MainWindowPresenter
{
	private final TxModel _model;
	private final TxView _view;

	public MainWindowPresenter(TxModel model, TxView view)
	{
		this._model = model;
		this._view = view;
	}

	public void onInit()
	{
		_view.displayTxCount(_model.getTxCount());
	}
}
