package gncimport.tests.unit;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.ConfigOptions;
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
	}

	
	@Test
	public void last_file_locations_can_be_retrieved_from_properties()
	{
		Properties properties = new Properties();
		properties.setProperty("last.gnc", "/path/to/file");
		
		ConfigOptions options = new ConfigOptions(properties);
		
		assertThat(options.getLastGncDirectory(), is("/path/to/file"));
	}
	
	@Test
	public void last_file_location_properties_can_be_left_blank()
	{
		Properties properties = new Properties();
		properties.setProperty("last.gnc", " ");
		
		ConfigOptions options = new ConfigOptions(properties);
		
		assertThat(options.getLastGncDirectory(), is(""));
	}
	
	@Test
	public void last_file_location_can_be_changed()
	{
		Properties properties = new Properties();
		properties.setProperty("last.gnc", "/path/to/file");
		
		ConfigOptions options = new ConfigOptions(properties);
		
		options.setLastGncDirectory("/new/path");
		
		assertThat(options.getProperties().getProperty("last.gnc"), is("/new/path"));
	}

	@Test
	public void last_file_location_can_be_set_to_blanks()
	{
		ConfigOptions options = new ConfigOptions(new Properties());
		
		options.setLastGncDirectory("   ");
		
		assertThat(options.getProperties().getProperty("last.gnc"), is(""));
	}


	@Test(expected = ProgrammerError.class)
	public void last_gnc_file_location_shouldnt_be_set_to_null()
	{
		ConfigOptions options = new ConfigOptions(new Properties());
		
		options.setLastGncDirectory(null);
	}
	
	@Test(expected = ProgrammerError.class)
	public void shouldnt_be_initialized_with_null()
	{
		new ConfigOptions(null);
	}
	
}
