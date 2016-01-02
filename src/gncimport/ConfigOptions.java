package gncimport;

import gncimport.models.PropertyModel;
import gncimport.models.TxMatcher;
import gncimport.transfer.MonthlyAccountParam;
import gncimport.ui.UIConfig;
import gncimport.utils.ProgrammerError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigOptions implements TxMatcher, UIConfig, PropertyModel
{
	public static final String LAST_CSV_KEY = "last.csv";
	public static final String LAST_GNC_KEY = "last.gnc";
	public static final String TX_REWRITE_RULE_KEY_REGEX = "match\\.[0-9]+\\.rewrite";
	public static final String ACC_OVERRIDE_RULE_KEY_REGEX = "match\\.[0-9]+\\.account";
	public static final String IGNORE_RULE_KEY_REGEX = "match\\.[0-9]+\\.ignore";
	public static final String MONTHLY_ACC_KEY_REGEX = "monthly\\.([0-9]+)";

	private class TxOverrideRule
	{
		public final String desc;
		public final String override;

		public TxOverrideRule(String desc, String account)
		{
			this.desc = desc;
			this.override = account;
		}
	}

	private List<TxOverrideRule> _accountOverrideRules = new ArrayList<TxOverrideRule>();
	private List<TxOverrideRule> _rewriteRule = new ArrayList<TxOverrideRule>();
	private List<String> _ignoreRules = new ArrayList<String>();
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
			MonthlyAccountParam p = new MonthlyAccountParam(Integer.parseInt(matcher.group(1)), value);
			
			_monthlyAccounts.add(p);
		}
	}

	private void createIgnoreRule(String key, String value)
	{
		if (key.matches(IGNORE_RULE_KEY_REGEX))
		{
			_ignoreRules.add(value);
		}
	}

	private void createTxOverrideRule(String key, String value)
	{		
		captureOverrideRuleIfApplicable(key, value, ACC_OVERRIDE_RULE_KEY_REGEX, _accountOverrideRules);
		captureOverrideRuleIfApplicable(key, value, TX_REWRITE_RULE_KEY_REGEX, _rewriteRule);
	}

	private void captureOverrideRuleIfApplicable(String key, String value, String propertyName, List<TxOverrideRule> ruleCollection)
	{
		String[] parts = value.split("\\|");

		if (key.matches(propertyName))
		{
			if (parts.length != 2)
			{
				throw new InvalidConfigOption("Invalid property format: " + value);
			}

			ruleCollection.add(new TxOverrideRule(parts[0], parts[1]));
		}
	}

	@Override
	public String findAccountOverride(String txDescription)
	{
		for (TxOverrideRule rule : _accountOverrideRules)
		{
			if (txDescription.trim().matches(rule.desc))
			{
				return rule.override;
			}
		}

		return null;
	}

	@Override
	public boolean isToBeIgnored(String txDescription)
	{
		for (String rule : _ignoreRules)
		{
			if (txDescription.trim().matches(rule))
			{
				return true;
			}
		}
		return false;
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
		for (String rule : _ignoreRules)
		{
			index++;
			builder.addTransactionIgnoreRule(index, rule);
		}
		
		index = 0;
		for (Iterator<TxOverrideRule> iterator = _accountOverrideRules.iterator(); iterator.hasNext();)
		{
			TxOverrideRule rule = iterator.next();
			
			index++;
			builder.addAccountMatchRule(index, rule.desc, rule.override);
		}		
		
		index = 0;
		for (Iterator<TxOverrideRule> iterator = _rewriteRule.iterator(); iterator.hasNext();)
		{
			TxOverrideRule rule = iterator.next();
			
			index++;
			builder.addDescRewriteRule(index, rule.desc, rule.override);
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
			String timmedTxDesc = txDescription.trim();
			
			if(timmedTxDesc.matches(rule.desc))
			{
				return timmedTxDesc.replaceAll(rule.desc, rule.override);
			}
		}
		return txDescription;
	}

	@Override
	public void replaceIgnoreRules(List<String> rules)
	{
		if(rules == null)
		{
			throw new IllegalArgumentException("rules cannot be null");
		}

		_ignoreRules = new ArrayList<String>(rules);
	}

	@Override
	public void copyIgnoreRules(List<String> rules)
	{
		if(rules == null)
		{
			throw new IllegalArgumentException("rules cannot be null");
		}
		
		rules.clear();
		rules.addAll(_ignoreRules);
	}
}
