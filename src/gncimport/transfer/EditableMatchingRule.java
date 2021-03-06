package gncimport.transfer;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditableMatchingRule extends MatchingRule
{
	private final ScreenValue _ruleExpression;
	
	private static final ScreenValueFactory Factory = new ScreenValueFactory()
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
	};
	
	public EditableMatchingRule(String ruleText)
	{
		_ruleExpression = Factory.newScreenValueFromText(ruleText, this);
	}

	@Override
	public boolean isValid()
	{
		return _ruleExpression.isValid();
	}

	@Override
	public String text()
	{
		return _ruleExpression.text();
	}

	@Override
	public ScreenValue asScreenValue()
	{
		return _ruleExpression;
	}
}