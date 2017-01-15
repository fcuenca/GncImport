package gncimport.ui.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public abstract class PropertyEditorPanel extends JPanel
{
	protected JTable _propertyTable;

	protected PropertyEditorPanel(JTable propTable)
	{
		_propertyTable = propTable;
		
		setLayout(new BorderLayout());
		
		add(new JScrollPane(_propertyTable), BorderLayout.PAGE_START);
		add(createToolBar(_propertyTable, (PropertyTableModel) _propertyTable.getModel()), BorderLayout.PAGE_END);
	}
	
	public void stopEditing()
	{
		if (_propertyTable.getCellEditor() != null) 
		{
		      _propertyTable.getCellEditor().stopCellEditing();
		}
	}
	
	protected abstract void addPanelSpecificToolbarControls(JToolBar toolBar);

	protected JToolBar createToolBar(final JTable theTable, final PropertyTableModel propTableModel)
	{
		JButton button;
		JToolBar toolBar = new JToolBar();
		
		button = new JButton("+");
		button.setToolTipText("Add");
		toolBar.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				propTableModel.newRow();
			}
		});
	
		button = new JButton("-");
		button.setToolTipText("Remove");
		toolBar.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				propTableModel.removeRow(theTable.getSelectedRow());
			}
		});
		
		toolBar.addSeparator();
		
		addPanelSpecificToolbarControls(toolBar);
	
		return toolBar;
	}
}