package gncimport.ui.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class EditPropertiesDialog extends JDialog
{
	public EditPropertiesDialog(Frame aFrame, TableModel ignoreTable)
	{
		super(aFrame, true);

		setLayout(new BorderLayout());
		setTitle("Property Editor");
		setName("PROP_EDITOR_DLG");
				
		add(createIgnoreListPanel(ignoreTable), BorderLayout.PAGE_START);
		add(createButtonPanel(), BorderLayout.PAGE_END);

		setupCloseOnESCkey();
		
		pack();
	}

	private JPanel createIgnoreListPanel(TableModel ignoreTable)
	{
		JPanel panel = new JPanel();
		JTable table = new JTable();
		
		table.setName("IGNORE_RULES");
		table.setModel(ignoreTable);
		
		panel.add(new JScrollPane(table));
		
		return panel;
	}

	private JPanel createButtonPanel()
	{
		JButton button;

		JPanel buttonPanel = new JPanel();

		button = new JButton("OK");
		button.setName("OK_BUTTON");
		buttonPanel.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onOkClicked();
			}
		});

		button = new JButton("Cancel");
		button.setName("CANCEL_BUTTON");
		buttonPanel.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onCancelClicked();
			}
		});

		return buttonPanel;
	}

	protected void onCancelClicked()
	{
		setVisible(false);
		dispose();
	}

	protected void onOkClicked()
	{
		setVisible(false);
		dispose();
	}

	public void setupCloseOnESCkey()
	{
		ActionListener escListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onCancelClicked();
			}
		};

		getRootPane().registerKeyboardAction(escListener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}
}
