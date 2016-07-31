package gncimport.ui.swing;

import gncimport.transfer.MatchingRule;
import gncimport.transfer.RuleTester;
import gncimport.transfer.UserEnteredMatchingRule;

import java.util.List;

@SuppressWarnings("serial")
public final class IgnoreRulesTableModel extends RuleTableModel
{
	public static final String[] COLUMN_TITLES = { "Description Pattern" };
	public static final Class<?>[] COLUMN_CLASSES = { MatchingRule.class };

	private final List<MatchingRule> _rules;
	private RuleTester _ruleTester;

	public IgnoreRulesTableModel(List<MatchingRule> ignoreRules, RuleTester tester)
	{
		if(ignoreRules == null) throw new IllegalArgumentException("Ignore Rule List cannot be null");
				
		this._rules = ignoreRules;
		this._ruleTester = tester;
	}

	@Override
	public int getColumnCount()
	{
		return COLUMN_TITLES.length;
	}

	@Override
	public int getRowCount()
	{
		return _rules.size();
	}

	@Override
	public String getColumnName(int col)
	{
		return COLUMN_TITLES[col];
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		return (MatchingRule)_rules.get(row);
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col)
	{
		_rules.set(row, (MatchingRule) value);
	}

	@Override
	public boolean isValid()
	{
		for (MatchingRule rule : _rules)
		{
			if(!rule.isValid()) return false;
		}
		return true;
	}

	@Override
	public Class<?> getColumnClass(int col)
	{
		return COLUMN_CLASSES[col];
	}

	@Override
	public void newRow()
	{
		_rules.add(new UserEnteredMatchingRule(""));
		fireTableDataChanged();
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

	public boolean testRulesWithText(String sampleText)
	{
		return _ruleTester.tryRulesWithText(sampleText, _rules);
	}
}