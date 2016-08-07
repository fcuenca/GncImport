package gncimport.transfer;

public interface TransactionRule
{
	public boolean isValid();
	public boolean matches(String someText);
	public String textForPossitiveMatch();
}