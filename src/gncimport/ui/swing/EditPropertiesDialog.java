package gncimport.ui.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class EditPropertiesDialog extends JDialog
{
	private boolean _okClicked;
	private PropertyEditorPanel _currentPanel;
	
	public EditPropertiesDialog(Frame aFrame, Map<String, PropertyEditorTableModel> models)
	{
		super(aFrame, true);

		setLayout(new BorderLayout());
		setTitle("Property Editor");
		setName("PROP_EDITOR_DLG");
						
		add(createTabs(models), BorderLayout.PAGE_START);
		add(createOkCancelButtonPanel(), BorderLayout.PAGE_END);

		setupCloseOnESCkey();
		
		pack();
	}

	private JTabbedPane createTabs(Map<String, PropertyEditorTableModel> models)
	{
		final JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.setName("PROPERTY_TABS");
						
		tabs.addTab("Ignore", new IgnoreRulesPanel(models.get("ignore")));
		tabs.addTab("Acc Override", new AccOverrideRulesPanel());
		
		_currentPanel = ((PropertyEditorPanel)tabs.getSelectedComponent());
		tabs.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				_currentPanel.stopEditing();
				_currentPanel= ((PropertyEditorPanel)tabs.getSelectedComponent());
			}
		});
		
		return tabs;
	}
	
	private JPanel createOkCancelButtonPanel()
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

	private void onCancelClicked()
	{
		setVisible(false);
		dispose();
	}

	private void onOkClicked()
	{
		_currentPanel.stopEditing();
		
		setVisible(false);
		dispose();
		
		_okClicked = true;
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

	public boolean editProperties()
	{
		_okClicked = false;
		
		setVisible(true);
				
		return _okClicked;
	}
}
