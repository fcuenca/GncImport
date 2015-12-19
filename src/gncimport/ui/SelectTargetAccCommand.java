package gncimport.ui;

import gncimport.interactors.AccSelectionInteractor;

class SelectTargetAccCommand implements Command<NoArgsEvent>
{
	private TxView _theView;
	private AccSelectionInteractor _theInteractor;

	public SelectTargetAccCommand(TxView view, AccSelectionInteractor interactor)
	{
		this._theView = view;
		this._theInteractor = interactor;
	}
	
	@Override
	public void execute(NoArgsEvent __not_used__)
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