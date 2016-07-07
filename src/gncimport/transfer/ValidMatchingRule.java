package gncimport.transfer;

public class ValidMatchingRule extends MatchingRule
{
	
	private String _text;
	
	protected ValidMatchingRule(String text)
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
	public MatchingRule copy()
	{
		return new ValidMatchingRule(_text);
	}

	@Override
	public String displayText()
	{
		return text();
	}
}