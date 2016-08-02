package gncimport.ui.swing;

import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;

import java.util.List;

@SuppressWarnings("serial")
public class AccOverrideRulesTableModel extends RuleTableModel
{
	public static final String[] COLUMN_TITLES = { "Description Pattern", "Account Override" };
	public static final Class<?>[] COLUMN_CLASSES = { MatchingRule.class,  MatchingRule.class };

	private List<OverrideRule> _rules;

	public AccOverrideRulesTableModel(List<OverrideRule> overrides)
	{
		if(overrides == null) throw new IllegalArgumentException("Override list cannot be null");
		_rules = overrides;
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
		OverrideRule rule = _rules.get(rowIndex);
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
	
	@Override
	public void setValueAt(Object value, int row, int col)
	{
		OverrideRule rule = _rules.get(row);
		OverrideRule updated;
		
		if(col == 0) updated = new OverrideRule((MatchingRule)value, rule.override);
		else updated = new OverrideRule(rule.textToMatch, (MatchingRule)value);
		
		_rules.set(row, updated);
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

	@Override
	public void newRow()
	{
		_rules.add(new OverrideRule("", ""));
		fireTableDataChanged();		
	}

	@Override
	public boolean isValid()
	{
		for (OverrideRule rule : _rules)
		{
			if(!rule.isValid()) return false;
		}
		return true;
	}
}
