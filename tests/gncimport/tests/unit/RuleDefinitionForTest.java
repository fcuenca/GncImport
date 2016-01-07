package gncimport.tests.unit;

import gncimport.transfer.RuleDefinition;

class RuleDefinitionForTest extends RuleDefinition
{
	private String _text;

	public RuleDefinitionForTest(String text)
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
	public RuleDefinition copy()
	{
		return new RuleDefinitionForTest(_text);
	}
	
}