package gncimport.models;

public class MonthlyAccountParam
{
	public final int code;
	public final String accName;

	public MonthlyAccountParam(int code, String accName)
	{
		this.code = code;
		this.accName = accName;		
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accName == null) ? 0 : accName.hashCode());
		result = prime * result + code;
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
		MonthlyAccountParam other = (MonthlyAccountParam) obj;
		if (accName == null)
		{
			if (other.accName != null)
				return false;
		}
		else if (!accName.equals(other.accName))
			return false;
		if (code != other.code)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "MonthlyAccountParam(" + code + ", " + accName + ")";
	}
	
	

}
