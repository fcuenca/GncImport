package gncimport.transfer;


public class OverrideRule
{
	public final MatchingRule desc;
	public final MatchingRule override;
	
	public OverrideRule(String matchingTxt, String overrideTxt)
	{
		this.desc = new UserEnteredMatchingRule(matchingTxt);
		this.override = new UserEnteredMatchingRule(overrideTxt);
	}
	
	public boolean isValid()
	{
		return desc.isValid() && override.isValid();
	}
	
	public boolean matches(String someText)
	{
		return desc.matches(someText);
	}
	
	public String getOverride()
	{
		return override.text();
	}
	
	public String applyOverrideTo(String txDescription)
	{
		String trimmedTxDesc = txDescription.trim();
		return trimmedTxDesc.replaceAll(desc.text(), override.text());		
	}
}