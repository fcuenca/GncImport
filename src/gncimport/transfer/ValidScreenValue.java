package gncimport.transfer;


public class ValidScreenValue extends AbstractScreenValue
{
	private String _text;
	
	protected ValidScreenValue(String text, Object domainValue)
	{
		super(domainValue);
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
	public String displayText()
	{
		return text();
	}
}