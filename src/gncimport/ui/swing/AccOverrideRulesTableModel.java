package gncimport.ui.swing;

import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;

import java.util.List;

@SuppressWarnings("serial")
public class AccOverrideRulesTableModel extends OverrideRuleTableModel
{
	public static final String[] COLUMN_TITLES = { "Description Pattern", "Account Override" };
	
	public AccOverrideRulesTableModel(List<OverrideRule> overrides, RuleTester tester)
	{
		super(COLUMN_TITLES, overrides, tester);
	}

}
