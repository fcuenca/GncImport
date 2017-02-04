package gncimport.ui.swing;

import gncimport.transfer.MonthlyAccount;

import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class MonthlyAccsEditorPanel extends PropertyEditorPanel
{

	public static class MonthlyAccTable extends StripePatternTable
	{
		public MonthlyAccTable(PropertyTableModel tm, String name)
		{
			super(tm, name);
			setDefaultRenderer(MonthlyAccount.class, new ValueRenderer());
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
