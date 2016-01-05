package gncimport.transfer;

import gncimport.utils.ProgrammerError;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class RuleDefinition
{
	protected RuleDefinition create(String text)
	{
		if(text == null)
		{
			throw new ProgrammerError("text cannot be null");
		}
		
		String errorMsg = validateText(text);
		
		if(errorMsg == null)
		{
			return new ValidRuleDefinition(text);
		}
		else
		{
			return new InvalidRuleDefinition(text, errorMsg);
		}
	}

	private String validateText(String text)
	{
		String errorMsg = null;
		if(text.trim().isEmpty())
		{
			errorMsg =  "Empty string is invalid";
		}
		else
		{
			try
			{
				Pattern.compile(text);
			}
			catch(PatternSyntaxException ex)
			{
				errorMsg =  "Invalid regex: " + ex.getDescription();
			}
		}
		return errorMsg;
	}
	
	public abstract boolean isValid();
	public abstract String text();
	public abstract String hint();
}