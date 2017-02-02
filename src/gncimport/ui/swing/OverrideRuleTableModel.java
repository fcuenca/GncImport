package gncimport.ui.swing;

import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;
import gncimport.transfer.TransactionRule;
import gncimport.transfer.WholeValue;

import java.util.List;

@SuppressWarnings("serial")
public abstract class OverrideRuleTableModel extends TxRuleTableModel
{
	public static final Class<?>[] COLUMN_CLASSES = { WholeValue.class,  WholeValue.class };

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
		final MatchingRule result = columnIndex == 0? rule.textToMatch : rule.override;
		return result.asScreenValue();
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