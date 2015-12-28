package gncimport.transfer;


public class AccountData
{
	private final String _accountName;
	private final String _accountId;
	private final String _parentId;

	public AccountData(String accName, String accId)
	{
		this(accName, accId, null);
	}

	public AccountData(String accName, String accId, String parentId)
	{
		if (accId == null || accId.isEmpty())
		{
			throw new IllegalArgumentException("Account ID value is null or empty for: " + accName);
		}

		this._accountName = accName;
		this._accountId = accId;
		this._parentId = parentId;
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

	public String getParentId()
	{
		return _parentId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_accountId == null) ? 0 : _accountId.hashCode());
		result = prime * result + ((_accountName == null) ? 0 : _accountName.hashCode());
		result = prime * result + ((_parentId == null) ? 0 : _parentId.hashCode());
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
		if (_parentId == null)
		{
			if (other._parentId != null)
				return false;
		}
		else if (!_parentId.equals(other._parentId))
			return false;
		return true;
	}

}