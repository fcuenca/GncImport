package gncimport.ui;

import gncimport.interactors.AccFileLoadInteractor;

public class LoadGncCommand extends LoadFileCommand  
{
	private UIConfig _theConfig;
	private AccFileLoadInteractor _theInteractor;

	public LoadGncCommand(TxView view, UIConfig config, AccFileLoadInteractor interactor)
	{
		super(view);
		this._theConfig = config;
		this._theInteractor = interactor;
	}

	@Override
	public String getLastUsedDirectory()
	{
		return _theConfig.getLastGncDirectory();
	}

	@Override
	public void loadFile(String fileName)
	{	
		_theInteractor.openGncFile(fileName);
	}
}