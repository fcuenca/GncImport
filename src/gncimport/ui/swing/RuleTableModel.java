package gncimport.ui.swing;

import gncimport.transfer.RuleTester;
import gncimport.transfer.TransactionRule;

import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public abstract class RuleTableModel extends AbstractTableModel
{
	protected List<? extends TransactionRule> _rules;
	private RuleTester _tester;

	protected String[] _columnTitles;

	public RuleTableModel(String[] colTitles, List<? extends TransactionRule> rules, RuleTester tester)
	{
		if(rules == null) throw new IllegalArgumentException("Rule List cannot be null");

		_columnTitles = colTitles;
		_rules = rules;
		_tester = tester;
	}
	
	public abstract void newRow();
	
	protected String[] getColumnTitles()
	{
		return _columnTitles;
	}
	
	@Override
	public int getColumnCount()
	{
		return getColumnTitles().length;
	}

	@Override
	public String getColumnName(int col)
	{
		return getColumnTitles()[col];
	}
	
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

	public String testRulesWithText(String sampleText)
	{
		return _tester.tryRulesWithText(sampleText, _rules);
	}

}