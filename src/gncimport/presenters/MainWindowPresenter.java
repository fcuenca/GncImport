package gncimport.presenters;

import gncimport.boundaries.MainWindowRenderer;
import gncimport.boundaries.TxModel;
import gncimport.boundaries.TxView;

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
	public void onInit()
	{
		_view.displayTxCount(_model.getTxCount());
	}
}
