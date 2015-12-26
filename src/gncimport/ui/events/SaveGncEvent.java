package gncimport.ui.events;

import gncimport.ui.Event;


public class SaveGncEvent extends Event
{
	public final String fileName;

	public SaveGncEvent(String fileName)
	{
		this.fileName = fileName;
	}

}
