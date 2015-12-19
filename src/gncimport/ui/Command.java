package gncimport.ui;

public interface Command<T extends Event>
{
	public void execute(T eventArgs);
}