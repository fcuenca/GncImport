package gncimport.ui;

import gncimport.interactors.AccSelectionInteractor;

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