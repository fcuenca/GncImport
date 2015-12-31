package gncimport.tests.unit;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import gncimport.ConfigOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ConfigOptionsImplementsPropertyModel
{
	private ConfigOptions _options;
	
	private Properties sampleProperties()
	{
		ConfigPropertyBuilder builder = new ConfigPropertyBuilder();
		
		builder.addTransactionIgnoreRule(1, "WEB TRANSFER");
		builder.addTransactionIgnoreRule(2, "MISC PAYMENT - RBC CREDIT CARD.*");
		
		Properties p = builder.build();
		
		return p;
	}

	
	@Before
	public void Setup()
	{
		_options = new ConfigOptions(sampleProperties());	
	}

	@Test
	public void provides_current_ignore_rules()
	{
		List<String> rules = new ArrayList<String>();
		
		_options.copyIgnoreRules(rules);
		
		assertThat(rules, hasSize(2));
		assertThat(rules, hasItems("WEB TRANSFER", "MISC PAYMENT - RBC CREDIT CARD.*"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void rejects_null_when_providing_ignore_rules()
	{
		_options.copyIgnoreRules(null);
	}
	
	@Test
	public void replaces_content_when_providing_ignore_rules()
	{
		List<String> rules = new ArrayList<String>(ListUtils.list_of("existing-1", "existing-2"));

		_options.copyIgnoreRules(rules);
		
		assertThat(rules, hasSize(2));
		assertThat(rules, not(hasItems("existing-1", "existing-2")));
	}

	@Test
	public void can_provide_empty_list_of_ignore_rules()
	{
		List<String> rules = new ArrayList<String>();

		_options = new ConfigOptions(new Properties());
		_options.copyIgnoreRules(rules);
		
		assertThat(rules, is(empty()));
	}
	
	@Test
	public void updates_ignore_rules_making_a_copy_of_provided_list()
	{
		List<String> newRules = new ArrayList<String>(ListUtils.list_of("new-rule-1", "MISC PAYMENT - RBC CREDIT CARD.*", "new-rule-2"));
		
		_options.replaceIgnoreRules(newRules);
		
		newRules.clear();
		
		List<String> updatedRules = new ArrayList<String>();
		
		_options.copyIgnoreRules(updatedRules);
		
		assertThat(updatedRules, hasSize(3));
		assertThat(updatedRules, hasItems("MISC PAYMENT - RBC CREDIT CARD.*", "new-rule-1", "new-rule-2"));

	}

	@Test(expected=IllegalArgumentException.class)
	public void rejects_null_when_updating_ignore_rules()
	{
		_options.replaceIgnoreRules(null);
	}

}
