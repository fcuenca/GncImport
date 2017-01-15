package gncimport.ui.swing;

import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;
import gncimport.transfer.TransactionRule;

import java.util.List;

@SuppressWarnings("serial")
public abstract class OverrideRuleTableModel extends RuleTableModel
{
	public static final Class<?>[] COLUMN_CLASSES = { MatchingRule.class,  MatchingRule.class };

	public OverrideRuleTableModel(String[] colTitles, List<? extends TransactionRule> rules, RuleTester tester)
	{
		super(colTitles, rules, tester);
	}

	@Override
	public Class<?> getColumnClass(int col)
	{
		return COLUMN_CLASSES[col];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		OverrideRule rule = (OverrideRule) _rules.get(rowIndex);
		return columnIndex == 0? rule.textToMatch : rule.override;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValueAt(Object value, int row, int col)
	{
		OverrideRule rule = (OverrideRule) _rules.get(row);
		OverrideRule updated;
		
		if(col == 0) updated = new OverrideRule((MatchingRule)value, rule.override);
		else updated = new OverrideRule(rule.textToMatch, (MatchingRule)value);
		
		((List<OverrideRule>)_rules).set(row, updated);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void newRow()
	{
		((List<OverrideRule>)_rules).add(new OverrideRule("", ""));
		fireTableDataChanged();		
	}

}