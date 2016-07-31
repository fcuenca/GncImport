package gncimport.ui.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class AccOverridePanel extends JPanel
{
	public AccOverridePanel()
	{
		super();
		setLayout(new BorderLayout());
		
		JTable table = new JTable();
		table.setName("ACC_OVERRIDE_RULES");
		
		add(new JScrollPane(table), BorderLayout.PAGE_START);
	}
}