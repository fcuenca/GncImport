package gncimport.ui.swing;

import gncimport.transfer.RuleDefinition;

import javax.swing.table.DefaultTableCellRenderer;

public class RuleDefCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 2707482275266150989L;

	@Override
	public void setValue(Object value)
	{
		RuleDefinition renderable = (RuleDefinition) value;
		setText(renderable.displayText());
	}
}