package gncimport.models;

import gncimport.transfer.TransactionRule;

import java.util.Map;

public interface RuleModel
{
	void replaceRulesWith(Map<String, Object> allRules);
	void copyRulesTo(Map<String, Object> allRules);
	
	public String testRulesWithText(String textToMatch, Iterable<? extends TransactionRule> rules);

}
