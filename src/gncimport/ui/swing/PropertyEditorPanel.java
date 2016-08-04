package gncimport.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;

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

	protected JToolBar createToolBar(final JTable theTable, final RuleTableModel ignoreTblModel)
	{
		JButton button;
		JToolBar toolBar = new JToolBar();
		
		button = new JButton("+");
		button.setToolTipText("Add new Rule");
		toolBar.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ignoreTblModel.newRow();
			}
		});
	
		button = new JButton("-");
		button.setToolTipText("Remove selected Rule");
		toolBar.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ignoreTblModel.removeRule(theTable.getSelectedRow());
			}
		});
		
		return toolBar;
	}
}