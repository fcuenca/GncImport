package gncimport.tests.data;

import static gncimport.tests.unit.ListUtils.list_of;
import gncimport.transfer.AccountData;

import java.util.List;

public class SampleAccountData
{
	public static List<AccountData> testAccountList()
	{
		return list_of(
				new AccountData("Root Account", "id-0", null),
				new AccountData("Parent 1", "id-1", "id-0"),
				new AccountData("Child 1", "id-2", "id-1"),
				new AccountData("Child 2", "id-3", "id-1"),
				new AccountData("Parent 2", "id-4", "id-0"));

	}

}
