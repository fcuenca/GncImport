package gncimport.models;

import gncimport.transfer.TransactionRule;

import java.util.Map;

public interface RuleModel
{
	void replaceRulesWith(Map<String, Object> allRules);
	void copyRulesTo(Map<String, Object> allRules);
	
	boolean testMatchingRulesWithText(String textToMatch, Iterable<? extends TransactionRule> candidateRules);
	public String testOverrideRulesWithText(String textToMatch, Iterable<? extends TransactionRule> rules);

}
