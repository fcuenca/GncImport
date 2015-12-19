package gncimport.ui;

public class CreateAccHierarchyEvent extends Event
{

	public final String fileNameToSave;

	public CreateAccHierarchyEvent(String fileNameToSave)
	{
		this.fileNameToSave = fileNameToSave;
	}

}
