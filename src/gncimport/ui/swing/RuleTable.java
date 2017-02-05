package gncimport.ui.swing;

import gncimport.transfer.ScreenValue;

@SuppressWarnings("serial")
public class RuleTable extends StripePatternTable
{	
	public RuleTable(TxRuleTableModel model, String name) 
	{		
		super(model, name);
		
		setDefaultRenderer(ScreenValue.class, new ValueRenderer());
		setDefaultEditor(ScreenValue.class, new ValueEditor());
	}
}