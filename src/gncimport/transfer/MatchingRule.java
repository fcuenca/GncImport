package gncimport.transfer;


public abstract class MatchingRule implements WholeValue, TransactionRule
{
	@Override
	public boolean matches(String someText)
	{
		return someText.trim().matches(text());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text() == null) ? 0 : text().hashCode());
		result = prime * result + new Boolean(isValid()).hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		MatchingRule other = (MatchingRule) obj;
		if(isValid() != other.isValid())
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
	
	@Override
	public String textForPossitiveMatch()
	{
		return "MATCH"; // This value is really arbitrary: nothing (including the tests) should rely on it for anything significant
		// HOWEVER: it's displayed in the UI when an ignore rule is tested, so the last statement is not *technically* correct :-/
		// This method is really needed so that other code operating on rules can treat them polymorphically.
		// The tests document current behaviour, and they don't rely on the returned value.
		// This is smelly, but I can't put my finger on it..... (maybe the problem is just the name?)
	}

	
}
