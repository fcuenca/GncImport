package gncimport.ui.swing;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class IgnoreRulesPanel extends PropertyEditorPanel
{
	public IgnoreRulesPanel(IgnoreRulesTableModel tableModel)
	{
		super();
		setLayout(new BorderLayout());

		_ruleTable = new RuleTable(tableModel, "IGNORE_RULES");
		
		add(new JScrollPane(_ruleTable), BorderLayout.PAGE_START);
		add(createToolBar(_ruleTable, tableModel), BorderLayout.PAGE_END);
	}

	@Override
	protected String testStringAgainstRules(String testString)
	{
		return ((IgnoreRulesTableModel) _ruleTable.getModel()).testRulesWithText((testString));
	}
}