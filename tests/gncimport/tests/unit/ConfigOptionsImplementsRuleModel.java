package gncimport.tests.unit;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import gncimport.ConfigOptions;
import gncimport.ConfigPropertyBuilder;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.MonthlyAccountParam;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleCategory;
import gncimport.transfer.UserEnteredMatchingRule;
import gncimport.utils.ProgrammerError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	@SuppressWarnings("unchecked")
	@Test
	public void provides_current_set_of_rules()
	{
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		
		_options.copyRulesTo(allRules);
		
		assertThat(allRules.size(), is(4));

		assertThat(allRules, hasKey(RuleCategory.ignore));
		assertThat(allRules, hasKey(RuleCategory.acc_override));
		assertThat(allRules, hasKey(RuleCategory.tx_override));
		assertThat(allRules, hasKey(RuleCategory.monthly_accs));

		assertThat(asTestRules((List<MatchingRule>) allRules.get(RuleCategory.ignore)), hasItems(
				new MatchingRuleForTest("WEB TRANSFER"), 
				new MatchingRuleForTest("MISC PAYMENT - RBC CREDIT CARD.*")));
		
		assertThat((List<OverrideRule>)allRules.get(RuleCategory.acc_override), hasItems(
				new OverrideRule("acc-desc-1", "acc-override-1"),
				new OverrideRule("acc-desc-2", "acc-override-2")));

		assertThat((List<OverrideRule>)allRules.get(RuleCategory.tx_override), hasItems(
				new OverrideRule("tx-desc-1", "tx-override-1"),
				new OverrideRule("tx-desc-2", "tx-override-2")));
		
		assertThat((List<MonthlyAccountParam>)allRules.get(RuleCategory.monthly_accs), hasItems(
				new MonthlyAccountParam(1, "month-1"),
				new MonthlyAccountParam(2, "month-2"),
				new MonthlyAccountParam(3, "month-3")));
}


	@Test(expected=IllegalArgumentException.class)
	public void rejects_null_when_providing_rules()
	{
		_options.copyRulesTo(null);
	}
	
	@Test
	public void replaces_content_when_providing_ignore_rules()
	{
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		allRules.put(RuleCategory.none, "some value"); // TODO: Hmmmm....
		
		_options.copyRulesTo(allRules);
		
		assertThat(allRules.size(), is(not(0)));
		assertThat(allRules, not(hasKey(RuleCategory.none)));
	}
		
	@SuppressWarnings("unchecked")
	@Test
	public void if_no_properties_are_defined_empty_rule_lists_are_returned()
	{
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		
		_options = new ConfigOptions(new Properties());
		_options.copyRulesTo(allRules);
				
		assertThat(allRules, hasKey(RuleCategory.ignore));
		assertThat((List<MatchingRule>)allRules.get(RuleCategory.ignore), is(empty()));
		assertThat(allRules, hasKey(RuleCategory.acc_override));
		assertThat((List<OverrideRule>)allRules.get(RuleCategory.acc_override), is(empty()));
		assertThat(allRules, hasKey(RuleCategory.tx_override));
		assertThat((List<OverrideRule>)allRules.get(RuleCategory.tx_override), is(empty()));
		assertThat(allRules, hasKey(RuleCategory.monthly_accs));
		assertThat((List<MonthlyAccountParam>)allRules.get(RuleCategory.monthly_accs), is(empty()));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void updates_ignore_rules_making_a_copy_of_provided_list()
	{
		List<MatchingRule> newIgnores = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("new-rule-1"), 
				new MatchingRuleForTest("MISC PAYMENT - RBC CREDIT CARD.*"), 
				new MatchingRuleForTest("new-rule-2")));
		
		List<OverrideRule> newAccOverrides = new ArrayList<OverrideRule>(ListUtils.list_of(
				new OverrideRule("new-desc", "new-override")));

		List<OverrideRule> newTxRewrites = new ArrayList<OverrideRule>(ListUtils.list_of(
				new OverrideRule("new-tx-desc", "new-tx-rewrite")));

		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		allRules.put(RuleCategory.ignore, newIgnores);
		allRules.put(RuleCategory.acc_override, newAccOverrides);
		allRules.put(RuleCategory.tx_override, newTxRewrites);
		
		_options.replaceRulesWith(allRules);
						
		Map<RuleCategory, Object> updatedAllRules = new HashMap<RuleCategory, Object>();

		_options.copyRulesTo(updatedAllRules);

		// just to make sure this list object is not kept inside the options object
		newIgnores.clear();
		newAccOverrides.clear();
		newTxRewrites.clear();
		allRules.clear(); 
		
		assertThat(asTestRules((List<MatchingRule>) updatedAllRules.get(RuleCategory.ignore)), hasItems(
				new MatchingRuleForTest("MISC PAYMENT - RBC CREDIT CARD.*"), 
				new MatchingRuleForTest("new-rule-1"), 
				new MatchingRuleForTest("new-rule-2")));
		
		assertThat((List<OverrideRule>)updatedAllRules.get(RuleCategory.acc_override), hasItems(
				new OverrideRule("new-desc", "new-override")));

		assertThat((List<OverrideRule>)updatedAllRules.get(RuleCategory.tx_override), hasItems(
				new OverrideRule("new-tx-desc", "new-tx-rewrite")));


		assertThat(allRules.size(), is(not(updatedAllRules.size())));
	}

	@Test(expected=IllegalArgumentException.class)
	public void rejects_null_when_updating_ignore_rules()
	{
		_options.replaceRulesWith(null);
	}
	
	@Test(expected=ProgrammerError.class)
	public void rejects_map_without_ignore_list()
	{
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		allRules.put(RuleCategory.acc_override, "irrelevant");
		
		_options.replaceRulesWith(allRules);
	}
	
	@Test(expected=ProgrammerError.class)
	public void rejects_map_without_accOverride_list()
	{
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		allRules.put(RuleCategory.ignore, "irrelevant");
		
		_options.replaceRulesWith(allRules);
	}

	@Test(expected=ProgrammerError.class)
	public void rejects_map_without_txRewite_list()
	{
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		allRules.put(RuleCategory.ignore, "irrelevant");
		allRules.put(RuleCategory.acc_override, "irrelevant");
		
		_options.replaceRulesWith(allRules);
	}
	

	@SuppressWarnings("unchecked")
	@Test
	public void updated_rules_added_to_property_file()
	{
		List<MatchingRule> newIgnores = new ArrayList<MatchingRule>(ListUtils.list_of(
				new UserEnteredMatchingRule("new-rule-1"), 
				new UserEnteredMatchingRule("MISC PAYMENT - RBC CREDIT CARD.*"), 
				new UserEnteredMatchingRule("new-rule-2")));
		
		List<OverrideRule> newAccOverrides = new ArrayList<OverrideRule>(ListUtils.list_of(
				new OverrideRule("new-desc", "new-override")));

		List<OverrideRule> newRewrites = new ArrayList<OverrideRule>(ListUtils.list_of(
				new OverrideRule("new-tx-desc", "new-tx-override")));
		
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		allRules.put(RuleCategory.ignore, newIgnores);
		allRules.put(RuleCategory.acc_override, newAccOverrides);
		allRules.put(RuleCategory.tx_override, newRewrites);

		_options.replaceRulesWith(allRules);
				
		ConfigOptions newConfig = new ConfigOptions(_options.getProperties());
		Map<RuleCategory, Object> newAllRules = new HashMap<RuleCategory, Object>();
		newConfig.copyRulesTo(newAllRules);
		
		assertThat((List<MatchingRule>)newAllRules.get(RuleCategory.ignore), containsInAnyOrder(newIgnores.toArray()));
		assertThat((List<OverrideRule>)newAllRules.get(RuleCategory.acc_override), containsInAnyOrder(newAccOverrides.toArray()));
		assertThat((List<OverrideRule>)newAllRules.get(RuleCategory.tx_override), containsInAnyOrder(newRewrites.toArray()));
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
	public void can_test_candidate_ignore_rules()
	{
		List<MatchingRule> rules = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("rule-1"), 
				new MatchingRuleForTest("rule-2")));

		assertThat(_options.testRulesWithText("rule-1", rules), is(not(nullValue())));
		assertThat(_options.testRulesWithText("doesn't match", rules), nullValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void candidate_ignore_rules_must_be_valid_when_testing_them()
	{
		List<MatchingRule> rules = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("rule-1"), 
				new MatchingRuleForTest("rule-2", false)));
		
		_options.testRulesWithText("rule-1", rules); // should throw
	}
		
	@Test
	public void can_test_override_rules()
	{
		List<OverrideRule> rules = new ArrayList<OverrideRule>(ListUtils.list_of(
				new OverrideRule("some text", "override")));
		
		assertThat(_options.testRulesWithText("doesn't match", rules), nullValue());
		assertThat(_options.testRulesWithText("some text", rules), is("override"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void candidate_override_rules_must_be_valid_when_testing_them()
	{
		List<OverrideRule> rules = new ArrayList<OverrideRule>(ListUtils.list_of(
				new OverrideRule("some text", "override"),
				new OverrideRule(new MatchingRuleForTest("invalid", false), new MatchingRuleForTest("some value"))));
		
		_options.testRulesWithText("some text", rules); //should throw
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

