package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsSame.sameInstance;
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
		
	@Test
	public void editing()
	{
		MonthlyAccount original = new MonthlyAccount(1, "Misc Expenses");
		
		MonthlyAccount edited = (MonthlyAccount) MonthlyAccount.Factory.editedValueFromText("new Acc Name", original.asScreenValue());
		
		assertThat(original, not(sameInstance(edited)));
		assertThat(edited, is(new MonthlyAccount(1, "new Acc Name")));	
	}



}
