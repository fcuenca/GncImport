package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.transfer.MonthlyAccount;
import gncimport.transfer.ScreenValue;
import gncimport.utils.ProgrammerError;

import org.junit.Test;

public class MonthlyAccountTests
{	
	@Test
	public void converts_to_screen_value()
	{
		MonthlyAccount value = new MonthlyAccount(5, "Car Expenses");
		
		assertThat(value.asScreenValue(), is((ScreenValue)new ScreenValueForTest("Car Expenses")));
	}

	@Test
	public void attaches_itself_to_screen_values()
	{
		MonthlyAccount value = new MonthlyAccount(1, "Misc Expenses");

		assertThat(value.asScreenValue().domainValue(), is((Object)value));
	}
		
	@Test
	public void acc_names_are_trimmed()
	{
		MonthlyAccount acc = new MonthlyAccount(1, "   Misc Expenses   ");
		
		assertThat(acc.getAccName(), is("Misc Expenses"));
	}

	@Test
	public void non_empty_account_names_are_valid()
	{
		MonthlyAccount acc = new MonthlyAccount(1, "Misc Expenses");
		assertThat(acc.isValid(), is(true));
		
		acc = new MonthlyAccount(2, "");
		assertThat(acc.isValid(), is(false));

		acc = new MonthlyAccount(3, "    ");
		assertThat(acc.isValid(), is(false));
	}
	
	@Test(expected=ProgrammerError.class)
	public void null_is_rejected()
	{
		new MonthlyAccount(5, null); // will throw
	}

}
