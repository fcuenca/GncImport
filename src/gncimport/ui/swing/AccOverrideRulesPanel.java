package gncimport.ui.swing;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class AccOverrideRulesPanel extends PropertyEditorPanel
{
	public AccOverrideRulesPanel(AccOverrideRulesTableModel tableModel)
	{
		super();
		setLayout(new BorderLayout());
		
		_ruleTable = new RuleTable(tableModel, "ACC_OVERRIDE_RULES");
		_ruleTable.setName("ACC_OVERRIDE_RULES");
		
		add(new JScrollPane(_ruleTable), BorderLayout.PAGE_START);
		add(createToolBar(_ruleTable, tableModel), BorderLayout.PAGE_END);
	}
	
	@Override
	protected String testStringAgainstRules(String testString)
	{
		return ((AccOverrideRulesTableModel) _ruleTable.getModel()).testRulesWithText(testString);
	}

}