package gncimport.ui.swing;

import javax.swing.JTable;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class MonthlyAccsEditorPanel extends PropertyEditorPanel
{

	public static class MonthlyAccTable extends JTable
	{
		public MonthlyAccTable(PropertyTableModel tm, String name)
		{
			super(tm);
			setName(name);
		}
	}
	
	protected MonthlyAccsEditorPanel(PropertyTableModel tm, String name)
	{
		super(new MonthlyAccTable(tm, name));
	}

	@Override
	protected void addPanelSpecificToolbarControls(JToolBar toolBar)
	{
		// TODO Auto-generated method stub

	}

}
