package gncimport.models;

import gncimport.transfer.MatchingRule;

import java.util.Map;

public interface RuleModel
{
	void replaceRulesWith(Map<String, Object> allRules);
	void copyRulesTo(Map<String, Object> allRules);
	boolean testRulesWithText(String text, Iterable<MatchingRule> candidateRules);
}
