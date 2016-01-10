package gncimport.ui.swing;

import gncimport.transfer.RuleDefinition;
import gncimport.transfer.UserEnteredRuleDefinition;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public final class PropertyEditorTableModel extends AbstractTableModel
{
	public static final String[] COLUMN_TITLES = { "Description Pattern" };
	public static final Class<?>[] COLUMN_CLASSES = { RuleDefinition.class };

	private final List<RuleDefinition> _rules;
	private static final long serialVersionUID = 9060984673285510233L;

	public PropertyEditorTableModel(List<RuleDefinition> rules)
	{
		this._rules = rules;
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
		return (RuleDefinition)_rules.get(row);
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col)
	{
		_rules.set(row, (RuleDefinition) value);
	}

	public boolean isValid()
	{
		for (RuleDefinition rule : _rules)
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
		_rules.add(new UserEnteredRuleDefinition(""));
		fireTableDataChanged();
	}
}