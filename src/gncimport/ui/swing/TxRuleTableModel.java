package gncimport.ui.swing;

import gncimport.transfer.RuleTester;
import gncimport.transfer.TransactionRule;

import java.util.List;

@SuppressWarnings("serial")
public abstract class TxRuleTableModel extends PropertyTableModel
{
	protected List<? extends TransactionRule> _rules;
	private RuleTester _tester;

	public TxRuleTableModel(String[] colTitles, List<? extends TransactionRule> rules, RuleTester tester)
	{
		super(colTitles);
		
		if(rules == null) throw new IllegalArgumentException("Rule List cannot be null");

		_rules = rules;
		_tester = tester;
	}
	
	@Override
	public abstract void newRow();
	
	@Override
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
	
	@Override
	public void removeRow(int row)
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