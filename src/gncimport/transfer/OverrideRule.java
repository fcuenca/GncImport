package gncimport.transfer;


public class OverrideRule
{
	public final MatchingRule textToMatch;
	public final MatchingRule override;
	
	public OverrideRule(String matchingTxt, String overrideTxt)
	{
		this.textToMatch = new UserEnteredMatchingRule(matchingTxt);
		this.override = new UserEnteredMatchingRule(overrideTxt);
	}
	
	public boolean isValid()
	{
		return textToMatch.isValid() && override.isValid();
	}
	
	public boolean matches(String someText)
	{
		return textToMatch.matches(someText);
	}
	
	public String applyOverrideTo(String txDescription)
	{
		String trimmedTxDesc = txDescription.trim();
		return trimmedTxDesc.replaceAll(textToMatch.text(), override.text());		
	}
}