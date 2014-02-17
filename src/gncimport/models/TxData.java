package gncimport.models;

import java.math.BigDecimal;
import java.util.Date;

public class TxData
{
	public final Date date;
	public final BigDecimal amount;
	public final String description;
	public AccountData targetAccount;

	public TxData(Date date, BigDecimal amount, String description)
	{
		this.date = date;
		this.amount = amount;
		this.description = description;
	}

	@Override
	public String toString()
	{
		return "[" + date + ", " + amount + ", " + description + ", " +
				targetAccount != null ? targetAccount.getId() : "null" + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((targetAccount == null || targetAccount.getId() == null) ? 0 : targetAccount.getId().hashCode());
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
		if (amount == null)
		{
			if (other.amount != null)
				return false;
		}
		// equals not used for amount because it includes comparing the scale!
		else if (amount.compareTo(other.amount) != 0)
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

		if (targetAccount == null)
		{
			if (other.targetAccount != null)
				return false;
		}
		else if (other.targetAccount == null || !targetAccount.getId().equals(other.targetAccount.getId()))
			return false;

		return true;
	}
}