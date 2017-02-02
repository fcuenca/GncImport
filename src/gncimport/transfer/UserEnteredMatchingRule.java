package gncimport.transfer;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class UserEnteredMatchingRule extends MatchingRule implements WholeValue
{
	private final WholeValue _backingRule;
	public static final WholeValueFactory Factory = new WholeValueFactory()
	{
		@Override
		public String validateStrRepresentation(String text)
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
		public MatchingRule newDomainObjectFromText(String text)
		{
			return new UserEnteredMatchingRule(text);
		}
	};
	
	public UserEnteredMatchingRule(String ruleText)
	{
		_backingRule = Factory.newScreenValueFromText(ruleText);
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

	@Override
	public WholeValue asScreenValue()
	{
		return this;
	}
}