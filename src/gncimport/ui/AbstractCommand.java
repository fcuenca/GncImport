package gncimport.ui;

public abstract class AbstractCommand<T extends Event>
{
	protected TxView _theView;

	public AbstractCommand()
	{
		super();
	}

	public AbstractCommand(TxView view)
	{
		_theView = view;
	}

	public void execute(T args)
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