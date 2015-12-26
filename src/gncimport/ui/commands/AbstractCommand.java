package gncimport.ui.commands;

import gncimport.ui.Event;
import gncimport.ui.TxView;

public abstract class AbstractCommand<T extends Event>
{
	protected TxView _theView;

	public AbstractCommand(TxView view)
	{
		_theView = view;
	}

	public final void execute(T args)
	{
		try
		{				
			doExecute(args);
		}
		catch (Exception e)
		{
			_theView.handleException(e);
		}
	}

	protected abstract void doExecute(T args);
}