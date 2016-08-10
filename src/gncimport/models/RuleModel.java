package gncimport.models;

import gncimport.transfer.RuleCategory;
import gncimport.transfer.TransactionRule;

import java.util.Map;

public interface RuleModel
{
	void replaceRulesWith(Map<RuleCategory, Object> allRules);
	void copyRulesTo(Map<RuleCategory, Object> allRules);
	
	public String testRulesWithText(String textToMatch, Iterable<? extends TransactionRule> rules);

}
