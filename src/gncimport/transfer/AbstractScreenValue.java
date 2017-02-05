package gncimport.transfer;


public abstract class AbstractScreenValue implements ScreenValue
{
	private final Object _domainValue;

	protected AbstractScreenValue(Object domainValue)
	{
		_domainValue = domainValue;
	}

	@Override
	public Object domainValue()
	{
		return _domainValue;
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