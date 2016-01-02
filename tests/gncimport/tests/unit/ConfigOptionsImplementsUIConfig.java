package gncimport.tests.unit;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import gncimport.ConfigOptions;
import gncimport.ConfigPropertyBuilder;
import gncimport.transfer.MonthlyAccountParam;
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
		properties.setProperty(ConfigOptions.LAST_GNC_KEY, "/path/to/gnc");
		properties.setProperty(ConfigOptions.LAST_CSV_KEY, "/path/to/csv");
		
		ConfigOptions options = new ConfigOptions(properties);
		
		assertThat(options.getLastGncDirectory(), is("/path/to/gnc"));
		assertThat(options.getLastCsvDirectory(), is("/path/to/csv"));
	}
	
	@Test
	public void last_file_location_properties_can_be_left_blank()
	{
		Properties properties = new Properties();
		properties.setProperty(ConfigOptions.LAST_GNC_KEY, " ");
		properties.setProperty(ConfigOptions.LAST_CSV_KEY, " ");
		
		ConfigOptions options = new ConfigOptions(properties);
		
		assertThat(options.getLastGncDirectory(), is(""));
		assertThat(options.getLastCsvDirectory(), is(""));
	}
	
	@Test
	public void last_file_location_can_be_changed()
	{
		Properties properties = new Properties();
		properties.setProperty(ConfigOptions.LAST_GNC_KEY, "/path/to/gnc");
		properties.setProperty(ConfigOptions.LAST_CSV_KEY, "/path/to/csv");
		
		ConfigOptions options = new ConfigOptions(properties);
		
		options.setLastGncDirectory("/new/path/gnc");
		options.setLastCsvDirectory("/new/path/csv");
		
		assertThat(options.getProperties().getProperty(ConfigOptions.LAST_GNC_KEY), is("/new/path/gnc"));
		assertThat(options.getProperties().getProperty(ConfigOptions.LAST_CSV_KEY), is("/new/path/csv"));
	}

	@Test
	public void last_file_location_can_be_set_to_blanks()
	{
		ConfigOptions options = new ConfigOptions(new Properties());
		
		options.setLastGncDirectory("   ");
		options.setLastCsvDirectory("   ");
		
		assertThat(options.getProperties().getProperty(ConfigOptions.LAST_GNC_KEY), is(""));
		assertThat(options.getProperties().getProperty(ConfigOptions.LAST_CSV_KEY), is(""));
	}
	
	@Test
	public void last_file_location_not_added_to_properties_if_it_wasnt_there()
	{
		ConfigOptions options = new ConfigOptions(new Properties());

		assertThat(options.getProperties().getProperty(ConfigOptions.LAST_GNC_KEY), is(nullValue()));
		assertThat(options.getProperties().getProperty(ConfigOptions.LAST_CSV_KEY), is(nullValue()));
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
		ConfigPropertyBuilder builder = new ConfigPropertyBuilder();
		builder.addSubAccountRule(1, "Gastos Varios");
		builder.addSubAccountRule(2, "Departamento");
		builder.addSubAccountRule(3, "Salud");
		
		ConfigOptions options = new ConfigOptions(builder.build());

		assertThat(options.getMonthlyAccounts(), hasSize(3));
		assertThat(options.getMonthlyAccounts(), hasItems(
				new MonthlyAccountParam(1, "Gastos Varios"),
				new MonthlyAccountParam(2, "Departamento"),
				new MonthlyAccountParam(3, "Salud")));
	}	
}
