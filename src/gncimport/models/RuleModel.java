package gncimport.models;

import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;

import java.util.Map;

public interface RuleModel
{
	void replaceRulesWith(Map<String, Object> allRules);
	void copyRulesTo(Map<String, Object> allRules);
	boolean testMatchingRulesWithText(String textToMatch, Iterable<MatchingRule> candidateRules);
	public String testOverrideRulesWithText(String textToMatch, Iterable<OverrideRule> rules);

}
