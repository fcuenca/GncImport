package gncimport.ui;

import gncimport.interactors.AccSelectionInteractor;

public class CreateAccHierarchyCommand implements Command<CreateAccHierarchyEvent>
{
	private TxView _theView;
	private AccSelectionInteractor _theInteractor;
	private UIConfig _theConfig;

	public CreateAccHierarchyCommand(TxView view, UIConfig config, AccSelectionInteractor interactor)
	{
		this._theView = view;
		this._theConfig = config;
		this._theInteractor = interactor;
	}

	@Override
	public void execute(CreateAccHierarchyEvent args)
	{
		if (args.fileNameToSave == null || args.fileNameToSave.trim().isEmpty())
		{
			_theView.displayErrorMessage("GNC file must be opened first!");
			return;
		}
	
		try
		{				
			 _theInteractor.createNewAccountHierarchy(_theConfig.getMonthlyAccounts(), args.fileNameToSave);
		}
		catch (Exception e)
		{
			_theView.handleException(e);
		}
	}
}