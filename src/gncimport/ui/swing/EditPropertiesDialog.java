package gncimport.ui.swing;

import gncimport.transfer.MatchingRule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class EditPropertiesDialog extends JDialog
{
	private boolean _okClicked;
	private JTable _table;
	
	class RulesTable extends JTable
	{
		final Color STANDARD_BACKGROUND_COLOR = Color.LIGHT_GRAY; 
		final Color ALTERNATE_BACKGROUND_COLOR = new Color(209, 229, 255);
		final Color SELECTION_BACKGROUND_COLOR = new Color(52, 117, 237);

		public RulesTable(PropertyEditorTableModel model) 
		{
			super(model);
			
			setDefaultRenderer(MatchingRule.class, new RuleDefCellRenderer());
			setDefaultEditor(MatchingRule.class, new RuleDefCellEditor());
			
			setName("IGNORE_RULES");
			setModel(model);
		}
		
		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
		{
			Component cell = super.prepareRenderer(renderer, row, column);

			if (isCellSelected(row, column)) cell.setBackground(SELECTION_BACKGROUND_COLOR);
			else if (alternatingRow(row)) cell.setBackground(ALTERNATE_BACKGROUND_COLOR);
			else cell.setBackground(STANDARD_BACKGROUND_COLOR);

			return cell;
		}
		
		private boolean alternatingRow(int row)
		{
			return row % 2 == 1;
		}
	}
	
	public EditPropertiesDialog(Frame aFrame, PropertyEditorTableModel tableModel)
	{
		super(aFrame, true);

		setLayout(new BorderLayout());
		setTitle("Property Editor");
		setName("PROP_EDITOR_DLG");
				
		add(createIgnoreListPanel(tableModel), BorderLayout.PAGE_START);
		add(createButtonPanel(), BorderLayout.PAGE_END);

		setupCloseOnESCkey();
		
		pack();
	}

	private JPanel createIgnoreListPanel(final PropertyEditorTableModel ignoreTable)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		_table = new RulesTable(ignoreTable);
		
		panel.add(new JScrollPane(_table), BorderLayout.PAGE_START);
		panel.add(createToolBar(ignoreTable), BorderLayout.PAGE_END);
		
		return panel;
	}

	private JToolBar createToolBar(final PropertyEditorTableModel ignoreTable)
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
				onAddRule(ignoreTable);
			}
		});

		button = new JButton("-");
		button.setToolTipText("Remove selected Rule");
		toolBar.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onRemoveRule(ignoreTable);
			}
		});
				
		toolBar.addSeparator();
		
		button = new JButton();
		button.setToolTipText("Try Rules");
		final JTextField textField = new JTextField();
		final JLabel resultLabel = new JLabel();
		final ImageIcon passIcon = new ImageIcon(getClass().getResource("pass.png"), "pass");
		final ImageIcon failIcon = new ImageIcon(getClass().getResource("fail.png"), "fail");
		
		button.setIcon(new ImageIcon(getClass().getResource("tryMe.png"), "try me"));
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(ignoreTable.testRulesWithText(textField.getText()))
				{
					resultLabel.setIcon(passIcon);
				}
				else
				{
					resultLabel.setIcon(failIcon);
				}
			}
		});
		
		textField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				resultLabel.setIcon(null);
				super.keyPressed(e);
			}
		});

		toolBar.add(button);
		toolBar.add(textField);
		toolBar.add(resultLabel);
		
		return toolBar;
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

	private void onCancelClicked()
	{
		setVisible(false);
		dispose();
	}

	private void onOkClicked()
	{
		if (_table.getCellEditor() != null) 
		{
		      _table.getCellEditor().stopCellEditing();
		}		
		
		setVisible(false);
		dispose();
		
		_okClicked = true;
	}

	private void onAddRule(PropertyEditorTableModel tableModel)
	{
		tableModel.newRow();
	}

	private void onRemoveRule(PropertyEditorTableModel tableModel)
	{
		tableModel.removeRule(_table.getSelectedRow());		
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
		
		System.out.println(_okClicked);
		
		return _okClicked;
	}
}
