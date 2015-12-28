package gncimport.transfer;

import gncimport.utils.ProgrammerError;

import java.util.Arrays;

public class Month
{
	public int month;

	public Month(int month)
	{
		if (month < 1 || month > 12)
		{
			throw new ProgrammerError("Invalid month value");
		}
		
		this.month = month;
	}

	public Month(String monthName)
	{
		this(Arrays.asList(Month.allMonths()).indexOf(monthName) + 1);
	}

	public String toNumericString()
	{
		return String.format("%02d", month);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + month;
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
		Month other = (Month) obj;
		if (month != other.month)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Month [" + month + "]";
	}

	public static String[] allMonths()
	{
		return new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	}
}
