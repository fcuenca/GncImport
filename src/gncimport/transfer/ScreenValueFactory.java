package gncimport.transfer;

import gncimport.utils.ProgrammerError;

public abstract class  ScreenValueFactory
{
	public abstract String validateStrRepresentation(String text);
	
	public abstract Object editedValueFromText(String text, ScreenValue originalValue);

	public ScreenValue newScreenValueFromText(String text)
	{
		if(text == null)
		{
			throw new ProgrammerError("text cannot be null");
		}
		
		String errorMsg = validateStrRepresentation(text);
		
		if(errorMsg == null)
		{
			return new ValidScreenValue(text.trim());
		}
		else
		{
			return new InvalidScreenValue(text, errorMsg);
		}
	}	
}