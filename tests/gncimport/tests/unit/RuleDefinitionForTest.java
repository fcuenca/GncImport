package gncimport.tests.unit;

import gncimport.transfer.RuleDefinition;

class RuleDefinitionForTest extends RuleDefinition
{
	private String _text;
	private boolean _isValid = true;

	public RuleDefinitionForTest(String text)
	{
		this._text = text;
	}

	public RuleDefinitionForTest(String text, boolean isValid)
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
	public RuleDefinition copy()
	{
		return new RuleDefinitionForTest(_text);
	}

	@Override
	public String displayText()
	{
		return "DISPLAY[" + _text + "]";
	}
	
}