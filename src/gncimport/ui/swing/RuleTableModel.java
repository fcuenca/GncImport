package gncimport.ui.swing;

import gncimport.transfer.TransactionRule;

import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public abstract class RuleTableModel extends AbstractTableModel
{
	protected List<? extends TransactionRule> _rules;

	public abstract void newRow();
	
	public boolean isValid()
	{
		for (TransactionRule rule : _rules)
		{
			if(!rule.isValid()) return false;
		}
		return true;

	}
	
	@Override
	public int getRowCount()
	{
		return _rules.size();
	}
	
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}
	
	public void removeRule(int row)
	{
		if(row >= 0 && row < _rules.size())
		{
			_rules.remove(row);
			fireTableDataChanged();
		}
	}

}