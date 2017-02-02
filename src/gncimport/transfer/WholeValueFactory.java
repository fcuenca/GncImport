package gncimport.transfer;

import gncimport.utils.ProgrammerError;

public abstract class  WholeValueFactory
{
	public abstract String validateStrRepresentation(String text);
	
	public abstract Object newDomainObjectFromText(String text);

	public WholeValue newScreenValueFromText(String text)
	{
		if(text == null)
		{
			throw new ProgrammerError("text cannot be null");
		}
		
		String errorMsg = validateStrRepresentation(text);
		
		if(errorMsg == null)
		{
			return new ValidWholeValue(text.trim());
		}
		else
		{
			return new InvalidWholeValue(text, errorMsg);
		}
	}	
}