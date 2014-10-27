package gncimport.tests.unit;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import gncimport.ConfigOptions;
import gncimport.models.MonthlyAccountParam;
import gncimport.utils.ProgrammerError;

import java.util.Properties;

import org.junit.Test;


public class ConfigOptionsImplementsUIConfig
{
	@Test
	public void last_file_locations_default_to_blank_string()
	{
		ConfigOptions options = new ConfigOptions(new Properties());
		
		assertThat(options.getLastGncDirectory(), is(""));
		assertThat(options.getLastCsvDirectory(), is(""));
	}

	
	@Test
	public void last_file_locations_can_be_retrieved_from_properties()
	{
		Properties properties = new Properties();
		properties.setProperty("last.gnc", "/path/to/gnc");
		properties.setProperty("last.csv", "/path/to/csv");
		
		ConfigOptions options = new ConfigOptions(properties);
		
		assertThat(options.getLastGncDirectory(), is("/path/to/gnc"));
		assertThat(options.getLastCsvDirectory(), is("/path/to/csv"));
	}
	
	@Test
	public void last_file_location_properties_can_be_left_blank()
	{
		Properties properties = new Properties();
		properties.setProperty("last.gnc", " ");
		properties.setProperty("last.csv", " ");
		
		ConfigOptions options = new ConfigOptions(properties);
		
		assertThat(options.getLastGncDirectory(), is(""));
		assertThat(options.getLastCsvDirectory(), is(""));
	}
	
	@Test
	public void last_file_location_can_be_changed()
	{
		Properties properties = new Properties();
		properties.setProperty("last.gnc", "/path/to/gnc");
		properties.setProperty("last.csv", "/path/to/csv");
		
		ConfigOptions options = new ConfigOptions(properties);
		
		options.setLastGncDirectory("/new/path/gnc");
		options.setLastCsvDirectory("/new/path/csv");
		
		assertThat(options.getProperties().getProperty("last.gnc"), is("/new/path/gnc"));
		assertThat(options.getProperties().getProperty("last.csv"), is("/new/path/csv"));
	}

	@Test
	public void last_file_location_can_be_set_to_blanks()
	{
		ConfigOptions options = new ConfigOptions(new Properties());
		
		options.setLastGncDirectory("   ");
		options.setLastCsvDirectory("   ");
		
		assertThat(options.getProperties().getProperty("last.gnc"), is(""));
		assertThat(options.getProperties().getProperty("last.csv"), is(""));
	}


	@Test(expected = ProgrammerError.class)
	public void last_gnc_file_location_shouldnt_be_set_to_null()
	{
		ConfigOptions options = new ConfigOptions(new Properties());
		
		options.setLastGncDirectory(null);
	}
	
	@Test(expected = ProgrammerError.class)
	public void last_csv_file_location_shouldnt_be_set_to_null()
	{
		ConfigOptions options = new ConfigOptions(new Properties());
		
		options.setLastCsvDirectory(null);
	}
	
	@Test(expected = ProgrammerError.class)
	public void shouldnt_be_initialized_with_null()
	{
		new ConfigOptions(null);
	}
	
	@Test
	public void provides_standard_hierarchy_account_names()
	{
		Properties properties = new Properties();
		properties.setProperty("monthly.1", "Gastos Varios");
		properties.setProperty("monthly.2", "Departamento");
		properties.setProperty("monthly.3", "Salud");
		
		ConfigOptions options = new ConfigOptions(properties);

		assertThat(options.getMonthlyAccounts(), hasSize(3));
		assertThat(options.getMonthlyAccounts(), hasItems(
				new MonthlyAccountParam(1, "Gastos Varios"),
				new MonthlyAccountParam(2, "Departamento"),
				new MonthlyAccountParam(3, "Salud")));
	}	
}
