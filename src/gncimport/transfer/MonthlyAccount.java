package gncimport.transfer;


public class MonthlyAccount
{
	public final int sequenceNo;
	private final ScreenValue _accName;

	private static final ScreenValueFactory Factory = new ScreenValueFactory()
	{
		@Override
		public String validateStrRepresentation(String text)
		{
			return null;
		}
	};
	

	public MonthlyAccount(int code, String accName)
	{
		this.sequenceNo = code;
		this._accName = Factory.newScreenValueFromText(accName, this);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getAccName() == null) ? 0 : getAccName().hashCode());
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
		if (getAccName() == null)
		{
			if (other.getAccName() != null)
				return false;
		}
		else if (!getAccName().equals(other.getAccName()))
			return false;
		if (sequenceNo != other.sequenceNo)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "MonthlyAccount(" + sequenceNo + ", " + getAccName() + ")";
	}

	public ScreenValue asScreenValue()
	{
		return _accName;
	}

	public String getAccName()
	{
		return _accName.text();
	}

}
