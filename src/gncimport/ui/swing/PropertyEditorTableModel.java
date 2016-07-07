package gncimport.ui.swing;

import gncimport.transfer.MatchingText;
import gncimport.transfer.RuleTester;
import gncimport.transfer.UserEnteredMatchingText;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public final class PropertyEditorTableModel extends AbstractTableModel
{
	public static final String[] COLUMN_TITLES = { "Description Pattern" };
	public static final Class<?>[] COLUMN_CLASSES = { MatchingText.class };

	private final List<MatchingText> _rules;
	private static final long serialVersionUID = 9060984673285510233L;
	private RuleTester _ruleTester;

	public PropertyEditorTableModel(List<MatchingText> rules, RuleTester tester)
	{
		this._rules = rules;
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
		return (MatchingText)_rules.get(row);
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col)
	{
		_rules.set(row, (MatchingText) value);
	}

	public boolean isValid()
	{
		for (MatchingText rule : _rules)
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

	public void newRow()
	{
		_rules.add(new UserEnteredMatchingText(""));
		fireTableDataChanged();
	}

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