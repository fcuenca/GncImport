package gncimport.ui.commands;

import gncimport.interactors.AccSelectionInteractor;
import gncimport.ui.Command;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;
import gncimport.ui.events.CreateAccHierarchyEvent;

public class CreateAccHierarchyCommand 
	extends AbstractCommand<CreateAccHierarchyEvent> implements Command<CreateAccHierarchyEvent>
{
	private AccSelectionInteractor _theInteractor;
	private UIConfig _theConfig;

	public CreateAccHierarchyCommand(TxView view, UIConfig config, AccSelectionInteractor interactor)
	{
		super(view);
		this._theConfig = config;
		this._theInteractor = interactor;
	}

	@Override
	protected void doExecute(CreateAccHierarchyEvent args)
	{
		if (args.fileNameToSave == null || args.fileNameToSave.trim().isEmpty())
		{
			_theView.displayErrorMessage("GNC file must be opened first!");
			return;
		}
		
		 _theInteractor.createNewAccountHierarchy(_theConfig.getMonthlyAccounts(), args.fileNameToSave);
	}
}