package gncimport.ui;

import gncimport.interactors.TxBrowseInteractor;

public class LoadCsvCommand  implements Command<NoArgsEvent>
{		
	private TxView _theView;
	private UIConfig _theConfig;
	private TxBrowseInteractor _theInteractor;

	public LoadCsvCommand(TxView view, UIConfig config, TxBrowseInteractor interactor)
	{
		_theView = view;
		_theConfig = config;
		_theInteractor = interactor;
	}

	@Override
	public void execute(NoArgsEvent __not_used__)
	{
		try
		{
			String lastDir = _theConfig.getLastCsvDirectory();
			
			if(lastDir == null || lastDir.isEmpty())
			{
				lastDir = System.getProperty("user.home");
			}
			
			final String fileName = _theView.promptForFile(lastDir);
			
			if (fileName != null)
			{					
				_theInteractor.fetchTransactions(fileName);				
			}
		}
		catch (Exception e)
		{
			_theView.handleException(e);
		}
	}
}