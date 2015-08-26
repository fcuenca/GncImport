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

		p.setProperty("match.1.rewrite", "MISC PAYMENT - GOODLIFE CLUBS|Gym membership");
		p.setProperty("match.2.rewrite", "MISC PAYMENT - IMH POOL I LP|Apartment Rent");
		p.setProperty("match.3.rewrite", "SAN CRISTOBAL.*|Home Insurance");
		p.setProperty("match.4.rewrite", "Withdrawal - PTB WD --- TZ(\\d*)|Cash withdrawal ($1)");

		p.setProperty("match.1.ignore", "WEB TRANSFER");
		p.setProperty("match.99.ignore", "MISC PAYMENT - RBC CREDIT CARD.*");

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
	
	@Test
	public void ignore_flag_not_set_if_theres_no_match()
	{
		assertThat(_options.isToBeIgnored("this will not be matched"), is(false));
	}
	
	@Test
	public void ignore_flag_set_when_theres_a_match()
	{
		assertThat(_options.isToBeIgnored("WEB TRANSFER  "), is(true));
		assertThat(_options.isToBeIgnored("MISC PAYMENT - RBC CREDIT CARD - TX123"), is(true));
	}

	@Test
	public void returns_description_as_is_when_theres_no_rewrite_rule()
	{
		assertThat(_options.rewriteDescription("TX DESCRIPTION"), is("TX DESCRIPTION"));
	}
	
	@Test
	public void rewrites_when_there_is_an_exact_match()
	{
		assertThat(_options.rewriteDescription("MISC PAYMENT - GOODLIFE CLUBS"), is("Gym membership"));
		assertThat(_options.rewriteDescription("MISC PAYMENT - IMH POOL I LP"), is("Apartment Rent"));
	}
	
	@Test
	public void rewrite_rules_can_be_regex()
	{
		assertThat(_options.rewriteDescription("SAN CRISTOBAL 123456"), is("Home Insurance"));
	}

	@Test
	public void rewrite_rules_can_use_capture_groups_in_regex()
	{
		assertThat(_options.rewriteDescription("Withdrawal - PTB WD --- TZ12345"), is("Cash withdrawal (12345)"));
	}
}
