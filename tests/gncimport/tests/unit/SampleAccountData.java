package gncimport.tests.unit;

import static gncimport.tests.unit.ListUtils.list_of;

import java.util.List;

import org.gnucash.xml.act.Id;
import org.gnucash.xml.act.Parent;
import org.gnucash.xml.gnc.Account;

public class SampleAccountData
{
	public static Account accountWith(String accName, String accId, String parentAccId)
	{
		Account account = new Account();
		account.setName(accName);

		if (accId != null)
		{
			Id id = new Id();
			id.setValue(accId);
			account.setId(id);
		}

		if (parentAccId != null)
		{
			Parent parent = new Parent();
			parent.setValue(parentAccId);
			account.setParent(parent);
		}

		return account;
	}

	public static List<Account> testAccountList()
	{
		return list_of(
				accountWith("Root Account", "id-0", null),
				accountWith("Parent 1", "id-1", "id-0"),
				accountWith("Child 1", "id-2", "id-1"),
				accountWith("Child 2", "id-3", "id-1"),
				accountWith("Parent 2", "id-4", "id-0"));

	}

}
