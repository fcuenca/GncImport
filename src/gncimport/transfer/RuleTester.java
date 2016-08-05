package gncimport.transfer;


public interface RuleTester
{
	boolean tryMatchingRulesWithText(String textToMatch, Iterable<? extends TransactionRule> candidateRules);
	String tryOverrideRulesWithText(String textToMatch, Iterable<? extends TransactionRule> candidateRules);
}