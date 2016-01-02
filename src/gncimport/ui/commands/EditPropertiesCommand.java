package gncimport.ui.commands;

import gncimport.interactors.PropertyEditInteractor;
import gncimport.ui.Command;
import gncimport.ui.TxView;
import gncimport.ui.events.NoArgsEvent;

public class EditPropertiesCommand
	extends AbstractCommand<NoArgsEvent> implements Command<NoArgsEvent>
{
	private PropertyEditInteractor _interactor;

	public EditPropertiesCommand(TxView view, PropertyEditInteractor interactor)
	{
		super(view);
		_interactor = interactor;
	}

	@Override
	protected void doExecute(NoArgsEvent __not_used__)
	{		
		_interactor.displayCurrentConfig();
	}

}
