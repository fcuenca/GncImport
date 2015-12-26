package gncimport.ui.commands;

import gncimport.ui.Command;
import gncimport.ui.TxView;
import gncimport.ui.events.NoArgsEvent;

public abstract class LoadFileCommand 
	extends AbstractCommand<NoArgsEvent> implements Command<NoArgsEvent>
{
	public LoadFileCommand(TxView view)
	{
		super(view);
	}

	@Override
	protected void doExecute(NoArgsEvent __not_used__)
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

	public abstract String getLastUsedDirectory();
	public abstract void loadFile(final String fileName);
}