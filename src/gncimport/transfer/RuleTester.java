package gncimport.transfer;


public interface RuleTester
{
	String tryRulesWithText(String textToMatch, Iterable<? extends TransactionRule> candidateRules);
}