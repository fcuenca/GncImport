package gncimport;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import gncimport.models.TxMatcher;
import gncimport.utils.InvalidConfigOption;

public class ConfigOptions implements TxMatcher
{
	private class TxMatchRule
	{
		public final String desc;
		public final String account;
		
		public TxMatchRule(String desc, String account)
		{
			this.desc = desc;
			this.account = account;
		}
	}
	
	private List<TxMatchRule> _rules;

	public ConfigOptions(Properties properties)
	{
		_rules = new ArrayList<TxMatchRule>();
		
		for (String key : properties.stringPropertyNames())
		{
			if (!key.matches("match\\.[0-9]+\\.account"))
			{
				continue;
			}
			
			String[] parts = properties.getProperty(key).split("\\|");
			
			if (parts.length != 2)
			{
				throw new InvalidConfigOption("Invalid property format: " + properties.getProperty(key));
			}
			
			_rules.add(new TxMatchRule(parts[0], parts[1]));
		}
	}

	@Override
	public String findAccountOverride(String txDescription)
	{
		for (TxMatchRule rule : _rules)
		{
			if (txDescription.trim().matches(rule.desc))
			{
				return rule.account;
			}			
		}

		return null;
	}
}
