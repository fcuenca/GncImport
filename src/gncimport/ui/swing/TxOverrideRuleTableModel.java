package gncimport.ui.swing;

import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;

import java.util.List;

@SuppressWarnings("serial")
public class TxOverrideRuleTableModel extends OverrideRuleTableModel
{
	public static final String[] COLUMN_TITLES = { "Description Pattern", "Description Override" };

	public TxOverrideRuleTableModel(List<OverrideRule> overrides, RuleTester tester)
	{
		super(COLUMN_TITLES, overrides, tester);
	}

}
