package gncimport.transfer;

public interface ScreenValue
{
	public abstract String hint();
	public abstract String displayText();
	public abstract boolean isValid();	
	public abstract String text();
	public abstract Object domainValue();	
}