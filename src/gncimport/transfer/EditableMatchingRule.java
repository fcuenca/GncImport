package gncimport.transfer;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditableMatchingRule extends MatchingRule
{
	private final ScreenValue _backingRule;
	
	public static final ScreenValueFactory Factory = new ScreenValueFactory()
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
			return new EditableMatchingRule(text);
		}
	};
	
	public EditableMatchingRule(String ruleText)
	{
		_backingRule = new UserEnteredScreenValue(Factory.newScreenValueFromText(ruleText));
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
	public ScreenValue asScreenValue()
	{
		return _backingRule;
	}
}