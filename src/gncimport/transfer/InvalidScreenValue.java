package gncimport.transfer;


public class InvalidScreenValue extends AbstractScreenValue
{
	private String _offendingText;
	private String _hint;

	protected InvalidScreenValue(String offendingText, String hint)
	{
		this._offendingText = offendingText;
		this._hint = hint;
	}

	@Override
	public boolean isValid()
	{
		return false;
	}

	@Override
	public String text()
	{
		return _offendingText;
	}

	@Override
	public String hint()
	{
		return _hint;
	}

	@Override
	public ScreenValue copy()
	{
		return new InvalidScreenValue(_offendingText, _hint);
	}

	@Override
	public String displayText()
	{
		return "<<" + _offendingText + ">>";
	}
}