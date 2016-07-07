package gncimport.transfer;

public class ValidMachingText extends MatchingText
{
	
	private String _text;
	
	protected ValidMachingText(String text)
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
	public MatchingText copy()
	{
		return new ValidMachingText(_text);
	}

	@Override
	public String displayText()
	{
		return text();
	}
}