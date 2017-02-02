package gncimport.transfer;

import gncimport.utils.ProgrammerError;

public class ValidWholeValue extends WholeValue
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

	@Override
	public String validateText(String text)
	{
		throw new ProgrammerError("ahhhh!");
	}
}