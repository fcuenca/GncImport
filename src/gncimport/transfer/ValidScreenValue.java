package gncimport.transfer;


public class ValidScreenValue implements ScreenValue
{
	private String _text;
	
	protected ValidScreenValue(String text)
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
	public ScreenValue copy()
	{
		return new ValidScreenValue(_text);
	}

	@Override
	public String displayText()
	{
		return text();
	}
}