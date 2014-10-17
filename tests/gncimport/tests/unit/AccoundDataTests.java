package gncimport.tests.unit;

import gncimport.models.AccountData;

import org.junit.Test;

public class AccoundDataTests
{
	@Test(expected = IllegalArgumentException.class)
	public void acccounts_should_have_an_id()
	{
		new AccountData("Root Node", null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void acount_ids_shouldnt_be_blank()
	{
		new AccountData("Root Node", "", null);
	}
}
