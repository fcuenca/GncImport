package gncimport.ui;

public abstract class LoadFileCommand implements Command<NoArgsEvent>
{
	private TxView _theView;

	public LoadFileCommand(TxView view)
	{
		_theView = view;
	}

	@Override
	public void execute(NoArgsEvent __not_used__)
	{
		try
		{
			String lastDir = getLastUsedDirectory();
			
			if(lastDir == null || lastDir.isEmpty())
			{
				lastDir = System.getProperty("user.home");
			}
			
			final String fileName = _theView.promptForFile(lastDir);
			
			if (fileName != null)
			{					
				loadFile(fileName);				
			}
		}
		catch (Exception e)
		{
			_theView.handleException(e);
		}
	}

	protected abstract String getLastUsedDirectory();
	protected abstract void loadFile(final String fileName);
}