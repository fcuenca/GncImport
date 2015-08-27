package gncimport.models;

public interface TxMatcher
{
	String findAccountOverride(String txDescription);
	boolean isToBeIgnored(String txDescription);
	String rewriteDescription(String txDescription);
}
