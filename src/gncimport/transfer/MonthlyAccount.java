package gncimport.transfer;

public class MonthlyAccount implements ScreenValue
{
	public final int sequenceNo;
	public final String accName;

	public MonthlyAccount(int code, String accName)
	{
		this.sequenceNo = code;
		this.accName = accName;		
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accName == null) ? 0 : accName.hashCode());
		result = prime * result + sequenceNo;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonthlyAccount other = (MonthlyAccount) obj;
		if (accName == null)
		{
			if (other.accName != null)
				return false;
		}
		else if (!accName.equals(other.accName))
			return false;
		if (sequenceNo != other.sequenceNo)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "MonthlyAccount(" + sequenceNo + ", " + accName + ")";
	}

	@Override
	public String hint()
	{
		return null;
	}

	@Override
	public String displayText()
	{
		return accName;
	}

	@Override
	public boolean isValid()
	{
		return true;
	}

	@Override
	public String text()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScreenValue copy()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
