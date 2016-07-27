package gncimport.tests.unit;

import gncimport.transfer.MatchingRule;

class MatchingRuleForTest extends MatchingRule
{
	private String _text;
	private boolean _isValid = true;

	public MatchingRuleForTest(String text)
	{
		this._text = text;
	}

	public MatchingRuleForTest(String text, boolean isValid)
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
	public MatchingRule copy()
	{
		return new MatchingRuleForTest(_text);
	}

	@Override
	public String displayText()
	{
		return "DISPLAY[" + _text + "]";
	}
	
}