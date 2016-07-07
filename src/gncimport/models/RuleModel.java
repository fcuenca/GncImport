package gncimport.models;

import gncimport.transfer.MatchingRule;

import java.util.List;

public interface RuleModel
{
	void replaceIgnoreRules(List<MatchingRule> rules);
	void copyIgnoreRules(List<MatchingRule> rules);
	boolean testRulesWithText(String text, Iterable<MatchingRule> candidateRules);
}
