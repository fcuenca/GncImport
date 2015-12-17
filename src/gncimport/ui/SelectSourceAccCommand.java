package gncimport.ui;

import gncimport.interactors.AccSelectionInteractor;

class SelectSourceAccCommand
{
	private TxView _theView;
	private AccSelectionInteractor _theInteractor;

	public SelectSourceAccCommand(TxView view, AccSelectionInteractor interactor)
	{
		this._theView = view;
		this._theInteractor = interactor;
	}

	public void execute()
	{
		try
		{
			_theInteractor.selectSourceAccount();
		}
		catch (Exception e)
		{
			_theView.handleException(e);
		}			
	}
}