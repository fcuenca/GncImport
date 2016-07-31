package gncimport.ui.swing;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class AccOverridePanel extends PropertyEditorPanel
{
	public AccOverridePanel()
	{
		super();
		setLayout(new BorderLayout());
		
		JTable table = new JTable();
		table.setName("ACC_OVERRIDE_RULES");
		
		add(new JScrollPane(table), BorderLayout.PAGE_START);
	}

	@Override
	public void stopEditing()
	{
		// TODO Auto-generated method stub
	}
}