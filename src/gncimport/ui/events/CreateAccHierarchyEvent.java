package gncimport.ui.events;

import gncimport.ui.Event;


public class CreateAccHierarchyEvent extends Event
{

	public final String fileNameToSave;

	public CreateAccHierarchyEvent(String fileNameToSave)
	{
		this.fileNameToSave = fileNameToSave;
	}

}
