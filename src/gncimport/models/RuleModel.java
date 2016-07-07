package gncimport.models;

import gncimport.transfer.MatchingRule;

import java.util.List;

public interface RuleModel
{
	void replaceRulesWith(List<MatchingRule> rules);
	void copyRulesTo(List<MatchingRule> rules);
	boolean testRulesWithText(String text, Iterable<MatchingRule> candidateRules);
}
