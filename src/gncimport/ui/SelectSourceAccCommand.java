package gncimport.ui;

import gncimport.interactors.AccSelectionInteractor;

public class SelectSourceAccCommand implements Command<NoArgsEvent>
{
	private TxView _theView;
	private AccSelectionInteractor _theInteractor;

	public SelectSourceAccCommand(TxView view, AccSelectionInteractor interactor)
	{
		this._theView = view;
		this._theInteractor = interactor;
	}

	@Override
	public void execute(NoArgsEvent __not_used__)
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