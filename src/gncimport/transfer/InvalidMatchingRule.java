package gncimport.transfer;

public class InvalidMatchingRule extends MatchingRule
{
	private String _offendingText;
	private String _hint;

	protected InvalidMatchingRule(String offendingText, String hint)
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
		return _offendingText;
	}

	@Override
	public String hint()
	{
		return _hint;
	}

	@Override
	public MatchingRule copy()
	{
		return new InvalidMatchingRule(_offendingText, _hint);
	}

	@Override
	public String displayText()
	{
		return "<<" + _offendingText + ">>";
	}
}