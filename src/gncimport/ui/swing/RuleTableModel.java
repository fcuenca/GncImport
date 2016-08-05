package gncimport.ui.swing;

import gncimport.transfer.TransactionRule;

import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public abstract class RuleTableModel extends AbstractTableModel
{
	protected List<? extends TransactionRule> _rules;

	public abstract void removeRule(int row);
	public abstract void newRow();
	
	public boolean isValid()
	{
		for (TransactionRule rule : _rules)
		{
			if(!rule.isValid()) return false;
		}
		return true;

	}
}