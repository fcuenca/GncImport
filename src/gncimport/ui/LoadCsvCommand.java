package gncimport.ui;

import gncimport.interactors.TxBrowseInteractor;

public class LoadCsvCommand extends LoadFileCommand  
{		
	private UIConfig _theConfig;
	private TxBrowseInteractor _theInteractor;

	public LoadCsvCommand(TxView view, UIConfig config, TxBrowseInteractor interactor)
	{
		super(view);
		_theConfig = config;
		_theInteractor = interactor;
	}

	@Override
	protected void loadFile(final String fileName)
	{
		_theInteractor.fetchTransactions(fileName);
	}

	@Override
	protected String getLastUsedDirectory()
	{
		return _theConfig.getLastCsvDirectory();
	}
}