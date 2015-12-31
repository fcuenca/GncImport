package gncimport;

import gncimport.models.PropertyModel;
import gncimport.models.TxMatcher;
import gncimport.transfer.MonthlyAccountParam;
import gncimport.ui.UIConfig;
import gncimport.utils.ProgrammerError;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigOptions implements TxMatcher, UIConfig, PropertyModel
{
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
	private Properties _properties;
	private List<MonthlyAccountParam> _monthlyAccounts = new ArrayList<MonthlyAccountParam>();
	private String _lastGnc;
	private String _lastCsv;

	public ConfigOptions(Properties properties)
	{
		if (properties == null) throw new ProgrammerError("Properties can't be null!!");
		
		_properties = properties;
		
		_lastGnc = _properties.getProperty("last.gnc");
		_lastCsv = _properties.getProperty("last.csv");
		
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
		Pattern pattern = Pattern.compile("monthly\\.([0-9]+)");
		Matcher matcher = pattern.matcher(key);
		
		if (matcher.matches())
		{
			MonthlyAccountParam p = new MonthlyAccountParam(Integer.parseInt(matcher.group(1)), value);
			
			_monthlyAccounts.add(p);
		}
	}

	private void createIgnoreRule(String key, String value)
	{
		if (key.matches("match\\.[0-9]+\\.ignore"))
		{
			_ignoreRules.add(value);
		}
	}

	private void createTxOverrideRule(String key, String value)
	{		
		captureOverrideRuleIfApplicable(key, value, "match\\.[0-9]+\\.account", _accountOverrideRules);
		captureOverrideRuleIfApplicable(key, value, "match\\.[0-9]+\\.rewrite", _rewriteRule);
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
		if(_lastCsv != null) _properties.setProperty("last.gnc", _lastGnc);
		if(_lastGnc != null) _properties.setProperty("last.csv", _lastCsv);
		
		ConfigPropertyBuilder builder = new ConfigPropertyBuilder();
		int index = 0;
		for (String rule : _ignoreRules)
		{
			index++;
			builder.addTransactionIgnoreRule(index, rule);
		}
		
		_properties.putAll(builder.build());
				
		return _properties;
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
