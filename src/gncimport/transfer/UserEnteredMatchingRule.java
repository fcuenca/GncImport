package gncimport.transfer;

public class UserEnteredMatchingRule extends MatchingRule
{
	private final MatchingRule _backingRule;
	
	public UserEnteredMatchingRule(String ruleText)
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