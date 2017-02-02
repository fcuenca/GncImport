package gncimport.ui.swing;

import gncimport.transfer.MatchingRule;
import gncimport.transfer.UserEnteredMatchingRule;

@SuppressWarnings("serial")
public class RuleTable extends StripePatternTable
{	
	public RuleTable(TxRuleTableModel model, String name) 
	{		
		super(model, name);
		
		setDefaultRenderer(MatchingRule.class, new ValueRenderer());
		setDefaultEditor(MatchingRule.class, new RuleDefCellEditor(UserEnteredMatchingRule.Factory));
	}
}