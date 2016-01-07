package gncimport.transfer;

public class InvalidRuleDefinition extends RuleDefinition
{
	private String _offendingText;
	private String _hint;

	protected InvalidRuleDefinition(String offendingText, String hint)
	{
		this._offendingText = offendingText;
		this._hint = hint;
	}

	@Override
	public boolean isValid()
	{
		return false;
	}

	@Override
	public String text()
	{
		return "<<" + _offendingText + ">>";
	}

	@Override
	public String hint()
	{
		return _hint;
	}

	@Override
	public RuleDefinition copy()
	{
		return new InvalidRuleDefinition(_offendingText, _hint);
	}
}