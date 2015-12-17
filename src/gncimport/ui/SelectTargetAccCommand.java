package gncimport.ui;

import gncimport.interactors.AccSelectionInteractor;

class SelectTargetAccCommand
{
	private TxView _theView;
	private AccSelectionInteractor _theInteractor;

	public SelectTargetAccCommand(TxView view, AccSelectionInteractor interactor)
	{
		this._theView = view;
		this._theInteractor = interactor;
	}

	public void execute()
	{
		try
		{
			_theInteractor.selectTargetAccount();				
		}
		catch (Exception e)
		{
			_theView.handleException(e);
		}			
	}
}