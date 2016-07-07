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
}