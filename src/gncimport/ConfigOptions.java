package gncimport;

import gncimport.models.TxMatcher;
import gncimport.utils.InvalidConfigOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigOptions implements TxMatcher
{
	private class TxAccountOverrideRule
	{
		public final String desc;
		public final String account;

		public TxAccountOverrideRule(String desc, String account)
		{
			this.desc = desc;
			this.account = account;
		}
	}

	private List<TxAccountOverrideRule> _accountOverrideRules = new ArrayList<TxAccountOverrideRule>();
	private List<String> _ignoreRules = new ArrayList<String>();

	public ConfigOptions(Properties properties)
	{
		for (String key : properties.stringPropertyNames())
		{
			String value = properties.getProperty(key);
			
			createAccountOverrideRule(key, value);
			createIgnoreRule(key, value);
		}
	}

	private void createIgnoreRule(String key, String value)
	{
		if (key.matches("match\\.[0-9]+\\.ignore"))
		{
			_ignoreRules.add(value);
		}
	}

	private void createAccountOverrideRule(String key, String value)
	{
		if (key.matches("match\\.[0-9]+\\.account"))
		{
			String[] parts = value.split("\\|");

			if (parts.length != 2)
			{
				throw new InvalidConfigOption("Invalid property format: " + value);
			}

			_accountOverrideRules.add(new TxAccountOverrideRule(parts[0], parts[1]));
		}
	}

	@Override
	public String findAccountOverride(String txDescription)
	{
		for (TxAccountOverrideRule rule : _accountOverrideRules)
		{
			if (txDescription.trim().matches(rule.desc))
			{
				return rule.account;
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
}
