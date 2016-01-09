package gncimport.tests.unit;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import gncimport.ConfigOptions;
import gncimport.ConfigPropertyBuilder;
import gncimport.transfer.RuleDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		
		builder.addSubAccountRule(1, "month-1");
		builder.addSubAccountRule(2, "month-2");
		builder.addSubAccountRule(3, "month-3");
		
		builder.addDescRewriteRule(1, "tx-desc-1", "tx-override-1");
		builder.addDescRewriteRule(2, "tx-desc-2", "tx-override-2");

		builder.addAccountMatchRule(1, "acc-desc-1", "acc-override-1");
		builder.addAccountMatchRule(2, "acc-desc-2", "acc-override-2");
	
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
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>();
		
		_options.copyIgnoreRules(rules);
		
		assertThat(rules, hasSize(2));
		
		assertThat(asTestRules(rules), hasItems(
				new RuleDefinitionForTest("WEB TRANSFER"), 
				new RuleDefinitionForTest("MISC PAYMENT - RBC CREDIT CARD.*")));
	}


	@Test(expected=IllegalArgumentException.class)
	public void rejects_null_when_providing_ignore_rules()
	{
		_options.copyIgnoreRules(null);
	}
	
	@Test
	public void replaces_content_when_providing_ignore_rules()
	{
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("existing-1"), 
				new RuleDefinitionForTest("existing-2")));
		
		_options.copyIgnoreRules(rules);
		
		assertThat(rules, hasSize(2));
		assertThat(asTestRules(rules), not(hasItems(new RuleDefinitionForTest("existing-1"), new RuleDefinitionForTest("existing-2"))));
	}
		
	@Test
	public void can_provide_empty_list_of_ignore_rules()
	{
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>();
		
		_options = new ConfigOptions(new Properties());
		_options.copyIgnoreRules(rules);
		
		assertThat(rules, is(empty()));
	}
	
	@Test
	public void updates_ignore_rules_making_a_copy_of_provided_list()
	{
		List<RuleDefinition> newRules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("new-rule-1"), 
				new RuleDefinitionForTest("MISC PAYMENT - RBC CREDIT CARD.*"), 
				new RuleDefinitionForTest("new-rule-2")));
		
		_options.replaceIgnoreRules(newRules);
						
		List<RuleDefinition> updatedRules = new ArrayList<RuleDefinition>();
		
		_options.copyIgnoreRules(updatedRules);
		
		assertThat(updatedRules, hasSize(3));
		assertThat(asTestRules(updatedRules), hasItems(
				new RuleDefinitionForTest("MISC PAYMENT - RBC CREDIT CARD.*"), 
				new RuleDefinitionForTest("new-rule-1"), 
				new RuleDefinitionForTest("new-rule-2")));
		
		newRules.clear(); // just to make sure this list object is not kept inside the options object
		assertThat(newRules.size(), is(not(updatedRules.size())));

	}

	@Test(expected=IllegalArgumentException.class)
	public void rejects_null_when_updating_ignore_rules()
	{
		_options.replaceIgnoreRules(null);
	}
	
	@Test
	public void updated_ignore_rules_added_to_property_file()
	{
		List<RuleDefinition> newRules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("new-rule-1"), 
				new RuleDefinitionForTest("MISC PAYMENT - RBC CREDIT CARD.*"), 
				new RuleDefinitionForTest("new-rule-2")));
		
		_options.replaceIgnoreRules(newRules);
		
		assertThatPropertiesMatchRuleDefinitions(newRules, _options.getProperties(), ConfigOptions.IGNORE_RULE_KEY_REGEX);
	}

	@Test
	public void monthly_account_list_is_added_to_property_file()
	{
		List<String> newRules = new ArrayList<String>(ListUtils.list_of("month-1", "month-2", "month-3"));

		assertMonthlyAccListMatchesListInOrder(newRules, _options.getProperties());
	}
	
	@Test
	public void rewrite_rules_added_to_the_properties_file()
	{
		List<String> newRules = new ArrayList<String>(ListUtils.list_of("tx-desc-1|tx-override-1", "tx-desc-2|tx-override-2"));
		
		assertThatPropertiesMatchList(newRules, _options.getProperties(), ConfigOptions.TX_REWRITE_RULE_KEY_REGEX);
	}

	@Test
	public void account_override_rules_added_to_the_properties_file()
	{
		List<String> newRules = new ArrayList<String>(ListUtils.list_of("acc-desc-1|acc-override-1", "acc-desc-2|acc-override-2"));
		
		assertThatPropertiesMatchList(newRules, _options.getProperties(), ConfigOptions.ACC_OVERRIDE_RULE_KEY_REGEX);
	}

	private void assertThatPropertiesMatchList(List<String> newRules, Properties properties, String propKeyRegex)
	{
		int propCount = 0;
		for (String key : properties.stringPropertyNames())
		{
			if (key.matches(propKeyRegex))
			{
				String value = properties.getProperty(key);

				assertThat("Unexpected property value found: " + value, newRules.indexOf(value), is(not(-1)));
				propCount++;
			}
		}
		assertThat("unxpected number of properties found", propCount, is(newRules.size()));
	}

	private void assertThatPropertiesMatchRuleDefinitions(List<RuleDefinition> newRules, Properties properties, String propKeyRegex)
	{
		int propCount = 0;
		for (String key : properties.stringPropertyNames())
		{
			if (key.matches(propKeyRegex))
			{
				String value = properties.getProperty(key);
				
				assertThat("Unexpected property value found: " + value, newRules.indexOf(new RuleDefinitionForTest(value)), is(not(-1)));
				propCount++;
			}
		}
		assertThat("unxpected number of properties found", propCount, is(newRules.size()));
	}
	
	private void assertMonthlyAccListMatchesListInOrder(List<String> newRules, Properties properties)
	{
		int propCount = 0;
		for (String key : properties.stringPropertyNames())
		{
			Pattern pattern = Pattern.compile(ConfigOptions.MONTHLY_ACC_KEY_REGEX);
			Matcher matcher = pattern.matcher(key);
			
			if (matcher.matches())
			{
				String value = properties.getProperty(key);
				int index = Integer.parseInt(matcher.group(1));

				assertThat("Unexpected property value found: " + value, newRules.indexOf(value), is(index - 1));
				propCount++;
			}
		}
		assertThat("unexpected number of properties found", propCount, is(newRules.size()));
	}
	
	@SuppressWarnings("unchecked")
	private List<RuleDefinitionForTest> asTestRules(List<RuleDefinition> rules)
	{
		return (List<RuleDefinitionForTest>)((List<?>)rules);
	}
}

