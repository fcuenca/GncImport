package gncimport.ui.swing;

import gncimport.transfer.MatchingRule;

@SuppressWarnings("serial")
public class RuleTable extends StripePatternTable
{	
	public RuleTable(TxRuleTableModel model, String name) 
	{		
		super(model, name);
		
		setDefaultRenderer(MatchingRule.class, new RuleDefCellRenderer());
		setDefaultEditor(MatchingRule.class, new RuleDefCellEditor());
	}
}