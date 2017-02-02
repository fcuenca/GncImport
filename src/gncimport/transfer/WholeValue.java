package gncimport.transfer;

import gncimport.utils.ProgrammerError;

public abstract class WholeValue
{
	public abstract String hint();
	public abstract String displayText();
	public abstract boolean isValid();
	
	public abstract String text();
	public abstract WholeValue copy();
	
	public abstract String validateText(String text);
	
	protected WholeValue create(String text)
	{
		if(text == null)
		{
			throw new ProgrammerError("text cannot be null");
		}
		
		String errorMsg = validateText(text);
		
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