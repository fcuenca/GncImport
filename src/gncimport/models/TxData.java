package gncimport.models;

public class TxData
{

	public final String date;
	public final double amount;
	public final String description;

	public TxData(String date, double amount, String description)
	{
		this.date = date;
		this.amount = amount;
		this.description = description;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		TxData other = (TxData) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (date == null)
		{
			if (other.date != null)
				return false;
		}
		else if (!date.equals(other.date))
			return false;
		if (description == null)
		{
			if (other.description != null)
				return false;
		}
		else if (!description.equals(other.description))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "[" + date + ", " + amount + ", " + description + "]";
	}
}