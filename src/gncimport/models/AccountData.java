package gncimport.models;

public class AccountData
{
	private final String _accountName;
	private final String _accountId;

	public AccountData(String accName, String accId)
	{
		this._accountName = accName;
		this._accountId = accId;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public String getName()
	{
		return _accountName;
	}

	public String getId()
	{
		return _accountId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_accountId == null) ? 0 : _accountId.hashCode());
		result = prime * result + ((_accountName == null) ? 0 : _accountName.hashCode());
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
		AccountData other = (AccountData) obj;
		if (_accountId == null)
		{
			if (other._accountId != null)
				return false;
		}
		else if (!_accountId.equals(other._accountId))
			return false;
		if (_accountName == null)
		{
			if (other._accountName != null)
				return false;
		}
		else if (!_accountName.equals(other._accountName))
			return false;
		return true;
	}

}