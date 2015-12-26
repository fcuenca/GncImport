package gncimport.ui.commands;

import gncimport.interactors.AccSelectionInteractor;
import gncimport.ui.Command;
import gncimport.ui.TxView;
import gncimport.ui.events.NoArgsEvent;

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