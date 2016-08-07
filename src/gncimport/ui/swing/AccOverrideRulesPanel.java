package gncimport.ui.swing;

import java.awt.BorderLayout;

import javax.swing.JLabel;
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
	
	protected boolean testStringAgainstRules(String testString, JLabel resultLabel)
	{
		String result = ((AccOverrideRulesTableModel) _ruleTable.getModel()).testRulesWithText(testString);
		
		resultLabel.setText(result);
		
		return result != null;
	}

}