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
	class RuleDefinitionForTest extends RuleDefinition
	{
		private String _text;

		public RuleDefinitionForTest(String text)
		{
			this._text = text;
		}

		@Override
		public boolean isValid()
		{
			return true;
		}

		@Override
		public String text()
		{
			return _text;
		}

		@Override
		public String hint()
		{
			return "";
		}

		@Override
		public RuleDefinition copy()
		{
			return new RuleDefinitionForTest(_text);
		}
		
	}
	
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
		List<String> rules = new ArrayList<String>();
		
		_options.copyIgnoreRules(rules);
		
		assertThat(rules, hasSize(2));
		assertThat(rules, hasItems("WEB TRANSFER", "MISC PAYMENT - RBC CREDIT CARD.*"));
	}
	
	@Test
	public void provides_current_ignore_rules2()
	{
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>();
		
		_options.copyIgnoreRules2(rules);
		
		assertThat(rules, hasSize(2));
		
		@SuppressWarnings("unchecked")
		List<RuleDefinitionForTest> rules2 = (List<RuleDefinitionForTest>)((List<?>)rules);
		
		assertThat(rules2, hasItems(
				new RuleDefinitionForTest("WEB TRANSFER"), 
				new RuleDefinitionForTest("MISC PAYMENT - RBC CREDIT CARD.*")));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void rejects_null_when_providing_ignore_rules()
	{
		_options.copyIgnoreRules(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void rejects_null_when_providing_ignore_rules2()
	{
		_options.copyIgnoreRules2(null);
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
	public void replaces_content_when_providing_ignore_rules2()
	{
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("existing-1"), 
				new RuleDefinitionForTest("existing-2")));
		
		_options.copyIgnoreRules2(rules);
		
		assertThat(rules, hasSize(2));
		@SuppressWarnings("unchecked")
		List<RuleDefinitionForTest> rules2 = (List<RuleDefinitionForTest>)((List<?>)rules);
		assertThat(rules2, not(hasItems(new RuleDefinitionForTest("existing-1"), new RuleDefinitionForTest("existing-2"))));
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
	public void can_provide_empty_list_of_ignore_rules2()
	{
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>();
		
		_options = new ConfigOptions(new Properties());
		_options.copyIgnoreRules2(rules);
		
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

	@Test
	public void updates_ignore_rules_making_a_copy_of_provided_list_2()
	{
		List<RuleDefinition> newRules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("new-rule-1"), 
				new RuleDefinitionForTest("MISC PAYMENT - RBC CREDIT CARD.*"), 
				new RuleDefinitionForTest("new-rule-2")));
		
		_options.replaceIgnoreRules2(newRules);
		
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
	
	@Test(expected=IllegalArgumentException.class)
	public void rejects_null_when_updating_ignore_rules_2()
	{
		_options.replaceIgnoreRules2(null);
	}
	
	@Test
	public void updated_ignore_rules_added_to_property_file2()
	{
		List<RuleDefinition> newRules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("new-rule-1"), 
				new RuleDefinitionForTest("MISC PAYMENT - RBC CREDIT CARD.*"), 
				new RuleDefinitionForTest("new-rule-2")));
		
		_options.replaceIgnoreRules2(newRules);
		
		assertThatPropertiesMatchList2(newRules, _options.getProperties(), ConfigOptions.IGNORE_RULE_KEY_REGEX);
	}

	@Test
	public void updated_ignore_rules_added_to_property_file()
	{
		List<String> newRules = new ArrayList<String>(ListUtils.list_of("new-rule-1", "MISC PAYMENT - RBC CREDIT CARD.*", "new-rule-2"));
		
		_options.replaceIgnoreRules(newRules);
		
		assertThatPropertiesMatchList(newRules, _options.getProperties(), ConfigOptions.IGNORE_RULE_KEY_REGEX);
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

	private void assertThatPropertiesMatchList2(List<RuleDefinition> newRules, Properties properties, String propKeyRegex)
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
}

