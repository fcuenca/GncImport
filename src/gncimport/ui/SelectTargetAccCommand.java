package gncimport.ui;

import gncimport.interactors.AccSelectionInteractor;

public class SelectTargetAccCommand 
	extends AbstractCommand<NoArgsEvent> implements Command<NoArgsEvent>
{
	private AccSelectionInteractor _theInteractor;

	public SelectTargetAccCommand(TxView view, AccSelectionInteractor interactor)
	{
		super(view);
		this._theInteractor = interactor;
	}
	
	@Override
	protected void doExecute(NoArgsEvent __not_used__)
	{
		_theInteractor.selectTargetAccount();
	}
}