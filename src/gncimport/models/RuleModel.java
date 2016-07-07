package gncimport.models;

import gncimport.transfer.MatchingText;

import java.util.List;

public interface RuleModel
{
	void replaceIgnoreRules(List<MatchingText> rules);
	void copyIgnoreRules(List<MatchingText> rules);
	boolean testRulesWithText(String text, Iterable<MatchingText> candidateRules);
}
