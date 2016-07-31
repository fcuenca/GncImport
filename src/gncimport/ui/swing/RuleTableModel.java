package gncimport.ui.swing;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public abstract class RuleTableModel extends AbstractTableModel
{
	public abstract void removeRule(int row);
	public abstract void newRow();
	public abstract boolean isValid();
}