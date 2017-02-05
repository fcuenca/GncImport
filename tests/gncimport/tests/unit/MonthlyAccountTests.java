package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.transfer.MonthlyAccount;
import gncimport.transfer.ScreenValue;

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
}
