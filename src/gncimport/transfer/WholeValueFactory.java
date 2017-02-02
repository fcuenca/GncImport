package gncimport.transfer;

import gncimport.utils.ProgrammerError;

public abstract class  WholeValueFactory
{
	public abstract String validateStrRepresentation(String text);
	
	public abstract MatchingRule ruleFromText(String text);

	public WholeValue valueFromText(String text)
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