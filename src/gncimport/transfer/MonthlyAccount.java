package gncimport.transfer;


public class MonthlyAccount
{
	public final int sequenceNo;
	public final String accName;
	private final ScreenValue _backing;

	
	public static final ScreenValueFactory Factory = new ScreenValueFactory()
	{
		@Override
		public String validateStrRepresentation(String text)
		{
			return null;
		}

		@Override
		public MonthlyAccount editedValueFromText(String text, ScreenValue originalValue)
		{
			return new MonthlyAccount(-1, text);
		}
	};
	

	public MonthlyAccount(int code, String accName)
	{
		this.sequenceNo = code;
		this.accName = accName;
		this._backing = Factory.newScreenValueFromText(accName);
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

	public ScreenValue asScreenValue()
	{
		return _backing;
	}

}
