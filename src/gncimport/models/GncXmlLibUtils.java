package gncimport.models;

import gncimport.transfer.AccountData;

import org.gnucash.xml.act.Id;
import org.gnucash.xml.gnc.Account;

public class GncXmlLibUtils
{
	public static AccountData fromAccount(final Account account)
	{
		GncXmlLibUtils.checkAccountIsValid(account);
	
		return new AccountData(
				account.getName(), account.getId().getValue(),
				account.getParent() != null ? account.getParent().getValue() : null);
	}

	static void checkAccountIsValid(final Account account)
	{
		if (account == null)
		{
			throw new IllegalArgumentException("account shouldn't be null");
		}
		else
		{
			Id id = account.getId();
	
			if (id != null)
			{
				if (id.getValue() == null || id.getValue().isEmpty())
				{
					throw new IllegalArgumentException("Account ID value is null or empty for: " + account.getName());
				}
			}
			else
			{
				throw new IllegalArgumentException("Account ID is null for: " + account.getName());
			}
		}
	}
}
