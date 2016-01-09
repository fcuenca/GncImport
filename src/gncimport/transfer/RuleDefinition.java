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
	public abstract String displayText();
	public abstract RuleDefinition copy();

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text() == null) ? 0 : text().hashCode());
		result = prime * result + new Boolean(isValid()).hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		RuleDefinition other = (RuleDefinition) obj;
		if(isValid() != other.isValid())
			return false;
		if (text() == null)
		{
			if (other.text() != null)
				return false;
		}
		else if (!text().equals(other.text()))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [" + isValid() + ", " + text() + "]";
	}
}
