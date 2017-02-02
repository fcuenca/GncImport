package gncimport.tests.unit;

import gncimport.transfer.WholeValue;

public class WholeValueForTest implements WholeValue
{
	private String _text;
	private boolean _isValid = true;
	
	public WholeValueForTest(String text)
	{
		this._text = text;
	}

	public WholeValueForTest(String text, boolean isValid)
	{
		this._text = text;
		this._isValid = isValid;
	}


	@Override
	public boolean isValid()
	{
		return _isValid;
	}

	@Override
	public String text()
	{
		return _text;
	}

	@Override
	public String hint()
	{
		return "HINT[" + _text + "]";
	}

	@Override
	public WholeValue copy()
	{
		return new WholeValueForTest(_text, _isValid);
	}

	@Override
	public String displayText()
	{
		return "DISPLAY[" + _text + "]";
	}	
}