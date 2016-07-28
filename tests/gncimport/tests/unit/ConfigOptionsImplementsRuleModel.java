package gncimport.tests.unit;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import gncimport.ConfigOptions;
import gncimport.ConfigPropertyBuilder;
import gncimport.transfer.MatchingRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class ConfigOptionsImplementsRuleModel
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
		List<MatchingRule> rules = new ArrayList<MatchingRule>();
		
		_options.copyRulesTo(rules, null);
		
		assertThat(rules, hasSize(2));
		
		assertThat(asTestRules(rules), hasItems(
				new MatchingRuleForTest("WEB TRANSFER"), 
				new MatchingRuleForTest("MISC PAYMENT - RBC CREDIT CARD.*")));
	}


	@Test(expected=IllegalArgumentException.class)
	public void rejects_null_when_providing_ignore_rules()
	{
		_options.copyRulesTo(null, null);
	}
	
	@Test
	public void replaces_content_when_providing_ignore_rules()
	{
		List<MatchingRule> rules = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("existing-1"), 
				new MatchingRuleForTest("existing-2")));
		
		_options.copyRulesTo(rules, null);
		
		assertThat(rules, hasSize(2));
		assertThat(asTestRules(rules), not(hasItems(new MatchingRuleForTest("existing-1"), new MatchingRuleForTest("existing-2"))));
	}
		
	@Test
	public void can_provide_empty_list_of_ignore_rules()
	{
		List<MatchingRule> rules = new ArrayList<MatchingRule>();
		
		_options = new ConfigOptions(new Properties());
		_options.copyRulesTo(rules, null);
		
		assertThat(rules, is(empty()));
	}
	
	@Test
	public void updates_ignore_rules_making_a_copy_of_provided_list()
	{
		List<MatchingRule> newRules = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("new-rule-1"), 
				new MatchingRuleForTest("MISC PAYMENT - RBC CREDIT CARD.*"), 
				new MatchingRuleForTest("new-rule-2")));
		
		_options.replaceRulesWith(newRules, null);
						
		List<MatchingRule> updatedRules = new ArrayList<MatchingRule>();
		
		_options.copyRulesTo(updatedRules, null);
		
		assertThat(updatedRules, hasSize(3));
		assertThat(asTestRules(updatedRules), hasItems(
				new MatchingRuleForTest("MISC PAYMENT - RBC CREDIT CARD.*"), 
				new MatchingRuleForTest("new-rule-1"), 
				new MatchingRuleForTest("new-rule-2")));
		
		newRules.clear(); // just to make sure this list object is not kept inside the options object
		assertThat(newRules.size(), is(not(updatedRules.size())));

	}

	@Test(expected=IllegalArgumentException.class)
	public void rejects_null_when_updating_ignore_rules()
	{
		_options.replaceRulesWith(null, null);
	}
	
	@Test
	public void updated_ignore_rules_added_to_property_file()
	{
		List<MatchingRule> newRules = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("new-rule-1"), 
				new MatchingRuleForTest("MISC PAYMENT - RBC CREDIT CARD.*"), 
				new MatchingRuleForTest("new-rule-2")));
		
		_options.replaceRulesWith(newRules, null);
		
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
	
	@Test
	public void can_test_candidate_properties()
	{
		List<MatchingRule> rules = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("rule-1"), 
				new MatchingRuleForTest("rule-2")));

		assertThat(_options.testRulesWithText("rule-1", rules), is(true));
		assertThat(_options.testRulesWithText("doesn't match", rules), is(false));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void candidate_rules_must_be_valid_when_testing_them()
	{
		List<MatchingRule> rules = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("rule-1"), 
				new MatchingRuleForTest("rule-2", false)));
		
		_options.testRulesWithText("rule-1", rules); // should throw
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

	private void assertThatPropertiesMatchRuleDefinitions(List<MatchingRule> newRules, Properties properties, String propKeyRegex)
	{
		int propCount = 0;
		for (String key : properties.stringPropertyNames())
		{
			if (key.matches(propKeyRegex))
			{
				String value = properties.getProperty(key);
				
				assertThat("Unexpected property value found: " + value, newRules.indexOf(new MatchingRuleForTest(value)), is(not(-1)));
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
	private List<MatchingRuleForTest> asTestRules(List<MatchingRule> rules)
	{
		return (List<MatchingRuleForTest>)((List<?>)rules);
	}
}

