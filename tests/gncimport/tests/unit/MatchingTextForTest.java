package gncimport.tests.unit;

import gncimport.transfer.MatchingText;

class MatchingTextForTest extends MatchingText
{
	private String _text;
	private boolean _isValid = true;

	public MatchingTextForTest(String text)
	{
		this._text = text;
	}

	public MatchingTextForTest(String text, boolean isValid)
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
	public MatchingText copy()
	{
		return new MatchingTextForTest(_text);
	}

	@Override
	public String displayText()
	{
		return "DISPLAY[" + _text + "]";
	}
	
}