package gncimport.transfer;


public interface RuleTester
{
	boolean tryRulesWithText(String textToMatch, Iterable<MatchingText> candidateRules);
}