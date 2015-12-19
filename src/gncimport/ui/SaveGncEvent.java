package gncimport.ui;

public class SaveGncEvent extends Event
{
	public final String fileName;

	public SaveGncEvent(String fileName)
	{
		this.fileName = fileName;
	}

}
