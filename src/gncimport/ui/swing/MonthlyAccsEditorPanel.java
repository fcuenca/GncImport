package gncimport.ui.swing;

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
		// TODO Auto-generated method stub

	}

}
