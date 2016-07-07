package gncimport;

import gncimport.models.RuleModel;
import gncimport.models.TxMatcher;
import gncimport.transfer.MonthlyAccountParam;
import gncimport.transfer.RuleDefinition;
import gncimport.transfer.UserEnteredRuleDefinition;
import gncimport.ui.UIConfig;
import gncimport.utils.ProgrammerError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigOptions implements TxMatcher, UIConfig, RuleModel
{
	public static final String LAST_CSV_KEY = "last.csv";
	public static final String LAST_GNC_KEY = "last.gnc";
	public static final String TX_REWRITE_RULE_KEY_REGEX = "match\\.[0-9]+\\.rewrite";
	public static final String ACC_OVERRIDE_RULE_KEY_REGEX = "match\\.[0-9]+\\.account";
	public static final String IGNORE_RULE_KEY_REGEX = "match\\.[0-9]+\\.ignore";
	public static final String MONTHLY_ACC_KEY_REGEX = "monthly\\.([0-9]+)";

	private class TxOverrideRule
	{
		public final RuleDefinition desc;
		public final RuleDefinition override;
		
		public TxOverrideRule(String desc, String account)
		{
			this.desc = new UserEnteredRuleDefinition(desc);
			this.override = new UserEnteredRuleDefinition(account);
		}
	}

	private List<TxOverrideRule> _accountOverrideRules = new ArrayList<TxOverrideRule>();
	private List<TxOverrideRule> _rewriteRule = new ArrayList<TxOverrideRule>();
	private List<RuleDefinition> _ignoreRules = new ArrayList<RuleDefinition>();
	private List<MonthlyAccountParam> _monthlyAccounts = new ArrayList<MonthlyAccountParam>();
	private String _lastGnc;
	private String _lastCsv;

	public ConfigOptions(Properties properties)
	{
		if (properties == null) throw new ProgrammerError("Properties can't be null!!");
				
		_lastGnc = properties.getProperty(LAST_GNC_KEY);
		_lastCsv = properties.getProperty(LAST_CSV_KEY);
		
		for (String key : properties.stringPropertyNames())
		{
			String value = properties.getProperty(key);
			
			createTxOverrideRule(key, value);
			createIgnoreRule(key, value);
			collectMonhtlyAccounts(key, value);			
		}
	}

	private void collectMonhtlyAccounts(String key, String value)
	{
		Pattern pattern = Pattern.compile(MONTHLY_ACC_KEY_REGEX);
		Matcher matcher = pattern.matcher(key);
		
		if (matcher.matches())
		{
			value = value.trim();
			
			if(value.isEmpty())
			{
				throw new InvalidConfigOption("Invalid property format: " + value);
			}
			
			MonthlyAccountParam p = new MonthlyAccountParam(Integer.parseInt(matcher.group(1)), value);
			
			_monthlyAccounts.add(p);
		}
	}

	private void createIgnoreRule(String key, String value)
	{
		if (key.matches(IGNORE_RULE_KEY_REGEX))
		{
			RuleDefinition def = new UserEnteredRuleDefinition(value);
			if(!def.isValid())
			{
				throw new InvalidConfigOption("Invalid property format: " + def.hint());
			}
			
			_ignoreRules.add(def);
		}
	}

	private void createTxOverrideRule(String key, String value)
	{		
		captureOverrideRuleIfApplicable(key, value, ACC_OVERRIDE_RULE_KEY_REGEX, _accountOverrideRules);
		captureOverrideRuleIfApplicable(key, value, TX_REWRITE_RULE_KEY_REGEX, _rewriteRule);
	}

	private void captureOverrideRuleIfApplicable(String key, String value, String propertyName, List<TxOverrideRule> ruleCollection)
	{
		if (key.matches(propertyName))
		{
			String[] parts = value.split("\\|");
			
			if (parts.length != 2)
			{
				throw new InvalidConfigOption("Invalid property format: " + value);
			}
			
			String desc = parts[0].trim();
			String override = parts[1].trim();
			
			if(desc.isEmpty() || override.isEmpty())
			{
				throw new InvalidConfigOption("Invalid property format: " + value);
			}

			ruleCollection.add(new TxOverrideRule(desc, override));
		}
	}

	@Override
	public String findAccountOverride(String txDescription)
	{
		for (TxOverrideRule rule : _accountOverrideRules)
		{
			if (rule.desc.matches(txDescription))
			{
				return rule.override.text();
			}
		}

		return null;
	}

	@Override
	public boolean isToBeIgnored(String txDescription)
	{
		return textMatchesRule(txDescription, _ignoreRules);
	}

	@Override
	public String getLastGncDirectory()
	{		
		return _lastGnc == null? "" : _lastGnc.trim();
	}

	@Override
	public void setLastGncDirectory(String path)
	{
		if (path == null) throw new ProgrammerError("Last GNC file location can't be null!!");

		_lastGnc =  path.trim();
	}

	public Properties getProperties()
	{
		Properties newProps = new Properties();
		
		if(_lastCsv != null) newProps.setProperty(LAST_GNC_KEY, _lastGnc);
		if(_lastGnc != null) newProps.setProperty(LAST_CSV_KEY, _lastCsv);
		
		newProps.putAll(buildPropsForRules());
				
		return newProps;
	}

	private Properties buildPropsForRules()
	{
		ConfigPropertyBuilder builder = new ConfigPropertyBuilder();
		
		int index = 0;
		for (RuleDefinition rule : _ignoreRules)
		{
			index++;
			builder.addTransactionIgnoreRule(index, rule.text());
		}
		
		index = 0;
		for (Iterator<TxOverrideRule> iterator = _accountOverrideRules.iterator(); iterator.hasNext();)
		{
			TxOverrideRule rule = iterator.next();
			
			index++;
			builder.addAccountMatchRule(index, rule.desc.text(), rule.override.text());
		}		
		
		index = 0;
		for (Iterator<TxOverrideRule> iterator = _rewriteRule.iterator(); iterator.hasNext();)
		{
			TxOverrideRule rule = iterator.next();
			
			index++;
			builder.addDescRewriteRule(index, rule.desc.text(), rule.override.text());
		}	
		
		for (Iterator<MonthlyAccountParam> iterator = _monthlyAccounts.iterator(); iterator.hasNext();)
		{
			MonthlyAccountParam rule = iterator.next();
			
			builder.addSubAccountRule(rule.sequenceNo, rule.accName);
		}		

		return builder.build();
	}

	@Override
	public String getLastCsvDirectory()
	{		
		return _lastCsv == null? "" : _lastCsv.trim();
	}

	@Override
	public void setLastCsvDirectory(String path)
	{
		if (path == null) throw new ProgrammerError("Last CSV file location can't be null!!");

		_lastCsv =  path.trim();
	}

	@Override
	public List<MonthlyAccountParam> getMonthlyAccounts()
	{
		return _monthlyAccounts;
	}

	@Override
	public String rewriteDescription(String txDescription)
	{
		for (TxOverrideRule rule : _rewriteRule)
		{
			String trimmedTxDesc = txDescription.trim();
			
			if(rule.desc.matches(trimmedTxDesc))
			{
				return trimmedTxDesc.replaceAll(rule.desc.text(), rule.override.text());
			}
		}
		return txDescription;
	}

	@Override
	public void replaceIgnoreRules(List<RuleDefinition> newRules)
	{
		if(newRules == null)
		{
			throw new IllegalArgumentException("rules cannot be null");
		}

		_ignoreRules = new ArrayList<RuleDefinition>(newRules);
	}

	@Override
	public void copyIgnoreRules(List<RuleDefinition> rules)
	{
		if(rules == null)
		{
			throw new IllegalArgumentException("rules cannot be null");
		}
		
		rules.clear();
		rules.addAll(_ignoreRules);
	}

	public boolean testRulesWithText(String text, Iterable<RuleDefinition> candidateRules)
	{
		for (RuleDefinition rule : candidateRules)
		{
			if(!rule.isValid())
			{
				throw new IllegalArgumentException("list contains invalid rule: " + rule.text());
			}
		};
		
		return textMatchesRule(text, candidateRules);
	}

	private boolean textMatchesRule(String txDescription, Iterable<RuleDefinition> rules)
	{
		for (RuleDefinition rule : rules)
		{
			if (rule.matches(txDescription))
			{
				return true;
			}
		}
		return false;
	}
}
