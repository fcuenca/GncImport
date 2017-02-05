package gncimport.tests.unit;

import gncimport.transfer.AbstractScreenValue;
import gncimport.transfer.ScreenValue;

public class ScreenValueForTest extends AbstractScreenValue 
{
	private String _text;
	private boolean _isValid = true;
	
	public ScreenValueForTest(String text)
	{
		this._text = text;
	}

	public ScreenValueForTest(String text, boolean isValid)
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
	public ScreenValue copy()
	{
		return new ScreenValueForTest(_text, _isValid);
	}

	@Override
	public String displayText()
	{
		return "DISPLAY[" + _text + "]";
	}
}