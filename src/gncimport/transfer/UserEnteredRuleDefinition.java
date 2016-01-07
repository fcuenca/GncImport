package gncimport.transfer;

public class UserEnteredRuleDefinition extends RuleDefinition
{
	private final RuleDefinition _backingRule;
	
	public UserEnteredRuleDefinition(String ruleText)
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
	public RuleDefinition copy()
	{
		return _backingRule.copy();
	}
}