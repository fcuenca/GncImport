package gncimport.ui.swing;

import gncimport.transfer.RuleDefinition;
import gncimport.transfer.UserEnteredRuleDefinition;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public final class PropertyEditorTableModel extends AbstractTableModel
{
	private final List<RuleDefinition> _rules;
	private static final long serialVersionUID = 9060984673285510233L;

	public PropertyEditorTableModel(List<RuleDefinition> rules)
	{
		this._rules = rules;
	}

	@Override
	public int getColumnCount()
	{
		return 1;
	}

	@Override
	public int getRowCount()
	{
		return _rules.size();
	}

	@Override
	public String getColumnName(int col)
	{
		return "Description Pattern";
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		RuleDefinition def = (RuleDefinition)_rules.get(row);
		
		return def.text();
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col)
	{
		_rules.set(row, new UserEnteredRuleDefinition((String) value));
	}

	public boolean isValid()
	{
		for (RuleDefinition rule : _rules)
		{
			if(!rule.isValid()) return false;
		}
		return true;
	}
}