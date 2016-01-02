package gncimport.ui.presenters;

import java.util.List;

import gncimport.ui.TxView;
import gncimport.interactors.PropertyEditInteractor;

public class PropertyEditorPresenter implements PropertyEditInteractor.OutPort
{

	private TxView _view;

	public PropertyEditorPresenter(TxView view)
	{
		this._view = view;
	}

	@Override
	public void editProperties(List<String> ignoreRules)
	{
		_view.editProperties(ignoreRules);
	}
}
