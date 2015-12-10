package gncimport.tests.unit;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import gncimport.models.Month;
import gncimport.utils.ProgrammerError;

import org.junit.Test;

public class MonthTests
{

	@Test(expected = ProgrammerError.class)
	public void months_start_at_1()
	{
		new Month(0);
	}

	@Test(expected = ProgrammerError.class)
	public void last_month_is_12()
	{
		new Month(13);
	}

	@Test
	public void formats_month_number_to_2_digits_padded_with_zero()
	{
		assertThat(new Month(5).toNumericString(), is("05"));
		assertThat(new Month(10).toNumericString(), is("10"));
	}
	
	@Test
	public void can_create_month_from_string()
	{
		assertThat(new Month("January"), is(new Month(1)));
	}
	
	@Test(expected = ProgrammerError.class)
	public void month_name_must_be_a_well_known_value()
	{
		new Month("unknown");
	}




}
