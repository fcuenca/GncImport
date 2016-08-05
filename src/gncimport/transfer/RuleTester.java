package gncimport.transfer;


public interface RuleTester
{
	boolean tryRulesWithText(String textToMatch, Iterable<MatchingRule> candidateRules);
	String tryOverrideRulesWithText(String textToMatch, Iterable<OverrideRule> candidateRules);
}