package gncimport;

import gncimport.models.MonthlyAccountParam;
import gncimport.models.TxMatcher;
import gncimport.ui.UIConfig;
import gncimport.utils.InvalidConfigOption;
import gncimport.utils.ProgrammerError;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigOptions implements TxMatcher, UIConfig
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
	private Properties _properties;
	private List<MonthlyAccountParam> _monthlyAccounts = new ArrayList<MonthlyAccountParam>();

	public ConfigOptions(Properties properties)
	{
		if (properties == null) throw new ProgrammerError("Properties can't be null!!");
		
		_properties = properties;
		
		for (String key : properties.stringPropertyNames())
		{
			String value = properties.getProperty(key);
			
			createAccountOverrideRule(key, value);
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

	@Override
	public String getLastGncDirectory()
	{
		String value = _properties.getProperty("last.gnc");
		
		return value == null? "" : value.trim();
	}

	@Override
	public void setLastGncDirectory(String path)
	{
		if (path == null) throw new ProgrammerError("Last GNC file location can't be null!!");

		_properties.setProperty("last.gnc", path.trim());
	}

	public Properties getProperties()
	{
		return _properties;
	}

	@Override
	public String getLastCsvDirectory()
	{
		String value = _properties.getProperty("last.csv");
		
		return value == null? "" : value.trim();
	}

	@Override
	public void setLastCsvDirectory(String path)
	{
		if (path == null) throw new ProgrammerError("Last CSV file location can't be null!!");

		_properties.setProperty("last.csv", path.trim());
	}

	@Override
	public List<MonthlyAccountParam> getMonthlyAccounts()
	{
		return _monthlyAccounts;
	}

	public String rewriteDescription(String txDescription)
	{
		return txDescription;
	}
}
