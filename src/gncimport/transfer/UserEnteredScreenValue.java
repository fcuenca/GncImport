package gncimport.transfer;

public class UserEnteredScreenValue extends AbstractScreenValue //TODO: this class adds nothing - REMOVE
{
	private final ScreenValue _backingValue;

	public UserEnteredScreenValue(ScreenValue backingValue)
	{
		super(null);
		_backingValue = backingValue;
	}
	
	@Override
	public String hint()
	{
		return _backingValue.hint();
	}

	@Override
	public String displayText()
	{
		return _backingValue.displayText();
	}

	@Override
	public boolean isValid()
	{
		return _backingValue.isValid();
	}

	@Override
	public String text()
	{
		return _backingValue.text();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (isValid() ? 1231 : 1237);
		result = prime * result + ((text() == null) ? 0 : text().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		ScreenValue other = (ScreenValue) obj;
		if (isValid() != other.isValid())
			return false;
		if (text() == null)
		{
			if (other.text() != null)
				return false;
		}
		else if (!text().equals(other.text()))
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [" + isValid() + ", " + text() + "]";
	}	
}