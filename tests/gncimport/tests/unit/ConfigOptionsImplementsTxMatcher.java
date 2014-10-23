package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
import gncimport.ConfigOptions;
import gncimport.utils.InvalidConfigOption;

import java.util.Properties;
import org.junit.Before;
import org.junit.Test;

public class ConfigOptionsImplementsTxMatcher
{
	private ConfigOptions _options;

	private Properties sampleProperties()
	{
		Properties p = new Properties();
		
		p.setProperty("match.1.account", "MISC PAYMENT - IMH POOL I LP|Departamento");
		p.setProperty("match.2.account", "MISC PAYMENT - GOODLIFE CLUBS|Salud");
		p.setProperty("match.99.account", "SAN CRISTOBAL SEG 1146ROSARIO.*|Casa Cordoba");
		p.setProperty("some.other.property", "this will not be matched|dummy");
		
		return p;
	}
	
	@Before
	public void Setup()
	{
		_options = new ConfigOptions(sampleProperties());	
	}
	
	@Test
	public void returns_null_when_there_is_no_match()
	{
		assertThat(_options.findAccountOverride("unmatched transaction"), is(nullValue()));
	}
	
	@Test
	public void returns_account_when_there_is_exact_match()
	{
		assertThat(_options.findAccountOverride("MISC PAYMENT - IMH POOL I LP"), is("Departamento"));
	}

	@Test
	public void trailing_blanks_ignored_when_matching_transctions()
	{
		assertThat(_options.findAccountOverride("MISC PAYMENT - GOODLIFE CLUBS    "), is("Salud"));
	}
	
	@Test
	public void can_accept_regex_in_matching_rules()
	{
		assertThat(_options.findAccountOverride("SAN CRISTOBAL SEG 1146ROSARIO --- TZ123"), is("Casa Cordoba"));
	}
	
	@Test
	public void only_matcher_properties_are_()
	{
		assertThat(_options.findAccountOverride("this will not be matched"), is(nullValue()));
	}
	
	@Test(expected = InvalidConfigOption.class)
	public void only_matching_rules_are_considered()
	{
		Properties p = new Properties();
		p.setProperty("match.1.account", "missing account name");
		
		new ConfigOptions(p); // this should throw
	}
	
	@Test
	public void matching_rules_are_optional_in_property_file()
	{
		_options = new ConfigOptions(new Properties());
		
		assertThat(_options.findAccountOverride("no match"), is(nullValue()));
	}
}
