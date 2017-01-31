package gncimport.transfer;

public abstract class WholeValue
{
	public abstract String hint();
	public abstract String displayText();
	public abstract boolean isValid();
	
	public abstract String text();
	public abstract WholeValue copy();

}