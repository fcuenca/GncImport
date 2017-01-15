package gncimport.ui.swing;

import gncimport.transfer.MatchingRule;
import gncimport.transfer.RuleTester;
import gncimport.transfer.UserEnteredMatchingRule;

import java.util.List;

@SuppressWarnings("serial")
public final class IgnoreRulesTableModel extends TxRuleTableModel
{
	public static final String[] COLUMN_TITLES = { "Description Pattern" };
	public static final Class<?>[] COLUMN_CLASSES = { MatchingRule.class };

	public IgnoreRulesTableModel(List<MatchingRule> ignoreRules, RuleTester tester)
	{
		super(COLUMN_TITLES, ignoreRules, tester);
	}

	@Override
	public Class<?> getColumnClass(int col)
	{
		return COLUMN_CLASSES[col];
	}
	
	@Override
	public Object getValueAt(int row, int col)
	{
		return _rules.get(row);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValueAt(Object value, int row, int col)
	{
		((List<MatchingRule>)_rules).set(row, (MatchingRule) value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void newRow()
	{
		((List<MatchingRule>)_rules).add(new UserEnteredMatchingRule(""));
		fireTableDataChanged();
	}
}