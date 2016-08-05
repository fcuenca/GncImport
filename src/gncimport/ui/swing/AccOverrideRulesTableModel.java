package gncimport.ui.swing;

import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;

import java.util.List;

@SuppressWarnings("serial")
public class AccOverrideRulesTableModel extends RuleTableModel
{
	public static final String[] COLUMN_TITLES = { "Description Pattern", "Account Override" };
	public static final Class<?>[] COLUMN_CLASSES = { MatchingRule.class,  MatchingRule.class };

	private RuleTester _tester;

	public AccOverrideRulesTableModel(List<OverrideRule> overrides, RuleTester tester)
	{
		if(overrides == null) throw new IllegalArgumentException("Override list cannot be null");
		_rules = overrides;
		_tester = tester;
	}

	@Override
	public int getColumnCount()
	{
		return COLUMN_TITLES.length;
	}
	
	@Override
	public String getColumnName(int col)
	{
		return COLUMN_TITLES[col];
	}

	@Override
	public int getRowCount()
	{
		return _rules.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		OverrideRule rule = (OverrideRule) _rules.get(rowIndex);
		return columnIndex == 0? rule.textToMatch : rule.override;
	}
	
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}

	@Override
	public Class<?> getColumnClass(int col)
	{
		return COLUMN_CLASSES[col];
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

	@Override
	public void removeRule(int row)
	{
		if(row >= 0 && row < _rules.size())
		{
			_rules.remove(row);
			fireTableDataChanged();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void newRow()
	{
		((List<OverrideRule>)_rules).add(new OverrideRule("", ""));
		fireTableDataChanged();		
	}

	@SuppressWarnings("unchecked")
	public String testRulesWithText(String textToMatch)
	{
		return _tester.tryOverrideRulesWithText(textToMatch, (Iterable<OverrideRule>)_rules);
	}
}