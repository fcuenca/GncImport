package gncimport.ui;

import gncimport.interactors.AccSelectionInteractor;

class CreateAccHierarchyCommand
{
	private String _fileNameToSave;
	private TxView _theView;
	private AccSelectionInteractor _theInteractor;
	private UIConfig _theConfig;

	public CreateAccHierarchyCommand(String fileNameToSave, TxView view, UIConfig config, AccSelectionInteractor interactor)
	{
		this._fileNameToSave = fileNameToSave;
		this._theView = view;
		this._theConfig = config;
		this._theInteractor = interactor;
	}

	public void execute()
	{
		if (_fileNameToSave == null || _fileNameToSave.trim().isEmpty())
		{
			_theView.displayErrorMessage("GNC file must be opened first!");
			return;
		}
	
		try
		{				
			 _theInteractor.createNewAccountHierarchy(_theConfig.getMonthlyAccounts(), _fileNameToSave);
		}
		catch (Exception e)
		{
			_theView.handleException(e);
		}
	}
}