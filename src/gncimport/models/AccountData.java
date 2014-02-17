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
}