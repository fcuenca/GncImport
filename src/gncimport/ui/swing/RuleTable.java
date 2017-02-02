package gncimport.ui.swing;

import gncimport.transfer.UserEnteredMatchingRule;
import gncimport.transfer.WholeValue;

@SuppressWarnings("serial")
public class RuleTable extends StripePatternTable
{	
	public RuleTable(TxRuleTableModel model, String name) 
	{		
		super(model, name);
		
		setDefaultRenderer(WholeValue.class, new ValueRenderer());
		setDefaultEditor(WholeValue.class, new ValueEditor(UserEnteredMatchingRule.Factory));
	}
}