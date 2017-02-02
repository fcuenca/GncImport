package gncimport.tests.unit;

import gncimport.transfer.ScreenValue;

public class ScreenValueForTest implements ScreenValue
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