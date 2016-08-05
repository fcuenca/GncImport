package gncimport.transfer;


public interface RuleTester
{
	boolean tryMatchingRulesWithText(String textToMatch, Iterable<MatchingRule> candidateRules);
	String tryOverrideRulesWithText(String textToMatch, Iterable<OverrideRule> candidateRules);
}