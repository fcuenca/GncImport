package gncimport.models;

import gncimport.transfer.MatchingRule;

import java.util.List;
import java.util.Map;

public interface RuleModel
{
	void replaceRulesWith(List<MatchingRule> rules, Map<String, Object> allRules);
	void copyRulesTo(List<MatchingRule> rules, Map<String, Object> allRules);
	boolean testRulesWithText(String text, Iterable<MatchingRule> candidateRules);
}
