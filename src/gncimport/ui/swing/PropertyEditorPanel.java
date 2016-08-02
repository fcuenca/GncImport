package gncimport.ui.swing;

import javax.swing.JPanel;
import javax.swing.JTable;

@SuppressWarnings("serial")
public abstract class PropertyEditorPanel extends JPanel
{
	protected JTable _ruleTable;

	public void stopEditing()
	{
		if (_ruleTable.getCellEditor() != null) 
		{
		      _ruleTable.getCellEditor().stopCellEditing();
		}
	}
}