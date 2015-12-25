package gncimport.ui;

public interface CommandFactory
{
	public <T extends Event> void registerEvent(String eventId, Command<T> command);
	public <T extends Event> void triggerWithArgs(T args);
	public void triggerWithoutArgs(String eventId);
}