package gncimport;

import gncimport.models.RuleModel;
import gncimport.models.TxMatcher;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.MonthlyAccount;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleCategory;
import gncimport.transfer.TransactionRule;
import gncimport.transfer.EditableMatchingRule;
import gncimport.ui.UIConfig;
import gncimport.utils.ProgrammerError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

	private List<OverrideRule> _accountOverrideRules = new ArrayList<OverrideRule>();
	private List<OverrideRule> _rewriteRules = new ArrayList<OverrideRule>();
	private List<MatchingRule> _ignoreRules = new ArrayList<MatchingRule>();
	private List<MonthlyAccount> _monthlyAccounts = new ArrayList<MonthlyAccount>();
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
			
			MonthlyAccount p = new MonthlyAccount(Integer.parseInt(matcher.group(1)), value);
			
			_monthlyAccounts.add(p);
		}
	}

	private void createIgnoreRule(String key, String value)
	{
		if (key.matches(IGNORE_RULE_KEY_REGEX))
		{
			EditableMatchingRule def = new EditableMatchingRule(value);
			if(!def.isValid())
			{
				throw new InvalidConfigOption("Invalid property format: " + def.asScreenValue().hint());
			}
			
			_ignoreRules.add(def);
		}
	}

	private void createTxOverrideRule(String key, String value)
	{		
		captureOverrideRuleIfApplicable(key, value, ACC_OVERRIDE_RULE_KEY_REGEX, _accountOverrideRules);
		captureOverrideRuleIfApplicable(key, value, TX_REWRITE_RULE_KEY_REGEX, _rewriteRules);
	}

	private void captureOverrideRuleIfApplicable(String key, String value, String propertyName, List<OverrideRule> ruleCollection)
	{
		if (key.matches(propertyName))
		{
			String[] parts = value.split("\\|");
			
			if (parts.length != 2)
			{
				throw new InvalidConfigOption("Invalid property format: " + value);
			}
			
			OverrideRule rule = new OverrideRule(parts[0], parts[1]);
			
			if(!rule.isValid())
			{
				throw new InvalidConfigOption("Invalid property format: " + value);
			}

			ruleCollection.add(rule);
		}
	}

	@Override
	public String findAccountOverride(String txDescription)
	{
		return getTextIfPossitiveMatch(txDescription, _accountOverrideRules);
	}

	@Override
	public boolean isToBeIgnored(String txDescription)
	{
		return getTextIfPossitiveMatch(txDescription, _ignoreRules) != null;
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
		for (MatchingRule rule : _ignoreRules)
		{
			index++;
			builder.addTransactionIgnoreRule(index, rule.text());
		}
		
		index = 0;
		for (Iterator<OverrideRule> iterator = _accountOverrideRules.iterator(); iterator.hasNext();)
		{
			OverrideRule rule = iterator.next();
			
			index++;
			builder.addAccountMatchRule(index, rule.textToMatch.text(), rule.override.text());
		}		
		
		index = 0;
		for (Iterator<OverrideRule> iterator = _rewriteRules.iterator(); iterator.hasNext();)
		{
			OverrideRule rule = iterator.next();
			
			index++;
			builder.addDescRewriteRule(index, rule.textToMatch.text(), rule.override.text());
		}	
		
		for (Iterator<MonthlyAccount> iterator = _monthlyAccounts.iterator(); iterator.hasNext();)
		{
			MonthlyAccount rule = iterator.next();
			
			builder.addSubAccountRule(rule.sequenceNo, rule.getAccName());
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
	public List<MonthlyAccount> getMonthlyAccounts()
	{
		return _monthlyAccounts;
	}

	@Override
	public String rewriteDescription(String txDescription)
	{
		for (OverrideRule rule : _rewriteRules)
		{			
			if(rule.matches(txDescription))
			{
				return rule.applyOverrideTo(txDescription);
			}
		}
		return txDescription;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void replaceRulesWith(Map<RuleCategory, Object> allRules)
	{
		if(allRules == null)
		{
			throw new IllegalArgumentException("rules cannot be null");
		}
		
		if (!(allRules.containsKey(RuleCategory.ignore) && 
			  allRules.containsKey(RuleCategory.acc_override) &&
			  allRules.containsKey(RuleCategory.tx_override) && 
			  allRules.containsKey(RuleCategory.monthly_accs)))
		{
			throw new ProgrammerError("Improper keys found in Rule Map: " + allRules.keySet());			
		}

	
		List<MatchingRule> newIgnores;
		newIgnores = (List<MatchingRule>) allRules.get(RuleCategory.ignore);
		_ignoreRules = new ArrayList<MatchingRule>(newIgnores);
		
		List<OverrideRule> newOverrides = (List<OverrideRule>)allRules.get(RuleCategory.acc_override);
		_accountOverrideRules = new ArrayList<OverrideRule>(newOverrides);	

		List<OverrideRule> newRewrites = (List<OverrideRule>)allRules.get(RuleCategory.tx_override);
		_rewriteRules = new ArrayList<OverrideRule>(newRewrites);	

		List<MonthlyAccount> newMonthlyAccs = (List<MonthlyAccount>)allRules.get(RuleCategory.monthly_accs);
		_monthlyAccounts = new ArrayList<MonthlyAccount>(newMonthlyAccs);	
}

	@Override
	public void copyRulesTo(Map<RuleCategory, Object> allRules)
	{
		
		if(allRules == null)
		{
			throw new IllegalArgumentException("rules cannot be null");
		}
		
		allRules.clear();
		allRules.put(RuleCategory.ignore, new ArrayList<MatchingRule>(_ignoreRules));
		allRules.put(RuleCategory.acc_override, new ArrayList<OverrideRule>(_accountOverrideRules));
		allRules.put(RuleCategory.tx_override, new ArrayList<OverrideRule>(_rewriteRules));
		allRules.put(RuleCategory.monthly_accs, new ArrayList<MonthlyAccount>(_monthlyAccounts));
	}

	@Override
	public String testRulesWithText(String textToMatch, Iterable<? extends TransactionRule> rules)
	{
		for (TransactionRule rule : rules)
		{
			if(!rule.isValid())
			{
				throw new IllegalArgumentException("list contains invalid rule: " + rule);
			}
		};
		return getTextIfPossitiveMatch(textToMatch, rules); 
	}

	private String getTextIfPossitiveMatch(String textToMatch, Iterable<? extends TransactionRule> rules)
	{
		for (TransactionRule rule : rules)
		{
			if (rule.matches(textToMatch))
			{
				return rule.textForPossitiveMatch();
			}
		}

		return null;
	}

}
