package gncimport.transfer;


public class TxOverrideRule
{
	public final RuleDefinition desc;
	public final RuleDefinition override;
	
	public TxOverrideRule(String desc, String account)
	{
		this.desc = new UserEnteredRuleDefinition(desc);
		this.override = new UserEnteredRuleDefinition(account);
	}
}