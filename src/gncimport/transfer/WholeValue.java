package gncimport.transfer;

public interface WholeValue
{
	public String hint();
	public String displayText();
	public boolean isValid();
	
	public String text();
	public WholeValue copy();

}