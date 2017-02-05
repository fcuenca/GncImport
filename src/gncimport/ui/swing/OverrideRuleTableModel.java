package gncimport.ui.swing;

import gncimport.transfer.EditableMatchingRule;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;
import gncimport.transfer.TransactionRule;
import gncimport.transfer.ScreenValue;

import java.util.List;

@SuppressWarnings("serial")
public abstract class OverrideRuleTableModel extends TxRuleTableModel
{
	public static final Class<?>[] COLUMN_CLASSES = { ScreenValue.class,  ScreenValue.class };

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
		MatchingRule editedValue = new EditableMatchingRule((String) value);
		
		if(col == 0) updated = new OverrideRule(editedValue, rule.override);
		else updated = new OverrideRule(rule.textToMatch, editedValue);
		
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