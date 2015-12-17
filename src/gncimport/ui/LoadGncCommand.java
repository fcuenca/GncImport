package gncimport.ui;

import gncimport.interactors.AccFileLoadInteractor;

class LoadGncCommand
{
	private TxView _theView;
	private UIConfig _theConfig;
	private AccFileLoadInteractor _theInteractor;

	public LoadGncCommand(TxView view, UIConfig config, AccFileLoadInteractor interactor)
	{
		this._theView = view;
		this._theConfig = config;
		this._theInteractor = interactor;
	}

	public void execute()
	{
		try
		{
			String lastGncDirectory = _theConfig.getLastGncDirectory();
			
			if(lastGncDirectory == null || lastGncDirectory.isEmpty())
			{
				lastGncDirectory = System.getProperty("user.home");
			}
			
			String fileName = _theView.promptForFile(lastGncDirectory);
			
			if (fileName != null)
			{
				_theInteractor.openGncFile(fileName);
			}
		}
		catch (Exception e)
		{
			_theView.handleException(e);
		}			
	}
}