package gncimport.ui.swing;

import gncimport.transfer.RuleDefinition;
import gncimport.transfer.UserEnteredRuleDefinition;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public final class PropertyEditorTableModel extends AbstractTableModel
{
	private final List<RuleDefinition> rules;
	private static final long serialVersionUID = 9060984673285510233L;

	public PropertyEditorTableModel(List<RuleDefinition> rules)
	{
		this.rules = rules;
	}

	@Override
	public int getColumnCount()
	{
		return 1;
	}

	@Override
	public int getRowCount()
	{
		return rules.size();
	}

	@Override
	public String getColumnName(int col)
	{
		return "Description Pattern";
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		RuleDefinition def = (RuleDefinition)rules.get(row);
		
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
		rules.set(row, new UserEnteredRuleDefinition((String) value));
	}
}