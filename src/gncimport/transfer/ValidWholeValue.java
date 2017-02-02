package gncimport.transfer;


public class ValidWholeValue implements WholeValue
{
	private String _text;
	
	protected ValidWholeValue(String text)
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
	public WholeValue copy()
	{
		return new ValidWholeValue(_text);
	}

	@Override
	public String displayText()
	{
		return text();
	}
}