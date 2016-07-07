package gncimport.transfer;


public class TxOverrideRule
{
	public final MatchingText desc;
	public final MatchingText override;
	
	public TxOverrideRule(String desc, String account)
	{
		this.desc = new UserEnteredMatchingText(desc);
		this.override = new UserEnteredMatchingText(account);
	}
}