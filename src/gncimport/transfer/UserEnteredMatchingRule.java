package gncimport.transfer;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import gncimport.utils.ProgrammerError;

public class UserEnteredMatchingRule extends MatchingRule
{
	private final WholeValue _backingRule;
	
	public UserEnteredMatchingRule(String ruleText)
	{
		_backingRule = create(ruleText);
	}

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
		


	@Override
	public boolean isValid()
	{
		return _backingRule.isValid();
	}

	@Override
	public String text()
	{
		return _backingRule.text();
	}

	@Override
	public String hint()
	{
		return _backingRule.hint();
	}

	@Override
	public WholeValue copy()
	{
		return _backingRule.copy();
	}

	@Override
	public String displayText()
	{
		return _backingRule.displayText();
	}
}