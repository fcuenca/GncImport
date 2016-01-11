package gncimport.models;

import gncimport.transfer.RuleDefinition;

import java.util.List;

public interface RuleModel
{
	void replaceIgnoreRules(List<RuleDefinition> rules);
	void copyIgnoreRules(List<RuleDefinition> rules);
	boolean testRulesWithText(String text, Iterable<RuleDefinition> candidateRules);
}
