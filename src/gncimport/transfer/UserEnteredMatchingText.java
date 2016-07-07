package gncimport.transfer;

public class UserEnteredMatchingText extends MatchingText
{
	private final MatchingText _backingRule;
	
	public UserEnteredMatchingText(String ruleText)
	{
		_backingRule = create(ruleText);
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
	public MatchingText copy()
	{
		return _backingRule.copy();
	}

	@Override
	public String displayText()
	{
		return _backingRule.displayText();
	}
}