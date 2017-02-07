package gncimport.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class MonthlyAccsEditorPanel extends PropertyEditorPanel
{
	protected MonthlyAccsEditorPanel(PropertyTableModel tm, String name)
	{
		super(new ScreenValueTable(tm, name));
	}

	@Override
	protected void addPanelSpecificToolbarControls(JToolBar toolBar)
	{
		JButton button;
		final MonthlyAccTableModel model = (MonthlyAccTableModel) _propertyTable.getModel();
		
		button = new JButton("Up");
		button.setToolTipText("Move up");
		toolBar.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int selection = _propertyTable.getSelectedRow(); 
				
				if (selection != 0)
				{
					model.moveUp(selection);
					_propertyTable.setRowSelectionInterval(selection - 1, selection - 1);
				}
			}
		});

		button = new JButton("Down");
		button.setToolTipText("Move down");
		toolBar.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//model.moveDown(theTable.getSelectedRow());
			}
		});
		
	}

}
