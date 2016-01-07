package gncimport.transfer;

public class ValidRuleDefinition extends RuleDefinition
{
	
	private String _text;
	
	protected ValidRuleDefinition(String text)
	{
		this._text = text;
	}

	@Override
	public boolean isValid()
	{
		return true;
	}

	@Override
	public String text()
	{
		return _text;
	}

	@Override
	public String hint()
	{
		return "";
	}

	@Override
	public RuleDefinition copy()
	{
		return new ValidRuleDefinition(_text);
	}
}