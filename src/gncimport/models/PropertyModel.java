package gncimport.models;

import gncimport.transfer.RuleDefinition;

import java.util.List;

public interface PropertyModel
{
	void replaceIgnoreRules(List<RuleDefinition> rules);
	void copyIgnoreRules(List<RuleDefinition> rules);
}
