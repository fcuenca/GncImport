package gncimport.transfer;


public class InvalidWholeValue implements WholeValue
{
	private String _offendingText;
	private String _hint;

	protected InvalidWholeValue(String offendingText, String hint)
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
	public WholeValue copy()
	{
		return new InvalidWholeValue(_offendingText, _hint);
	}

	@Override
	public String displayText()
	{
		return "<<" + _offendingText + ">>";
	}
}