package gncimport.ui.swing;

import gncimport.transfer.RuleDefinition;
import gncimport.transfer.UserEnteredRuleDefinition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class EditPropertiesDialog extends JDialog
{
	private boolean _okClicked;
	
	class RulesTable extends JTable
	{
		final Color STANDARD_BACKGROUND_COLOR = Color.LIGHT_GRAY; 
		final Color ALTERNATE_BACKGROUND_COLOR = new Color(209, 229, 255);
		final Color SELECTION_BACKGROUND_COLOR = new Color(52, 117, 237);

		public RulesTable(TableModel model) 
		{
			super(model);
			
			setDefaultRenderer(RuleDefinition.class, cellRenderer());
			setDefaultEditor(RuleDefinition.class, cellEditor());
			
			setName("IGNORE_RULES");
			setModel(model);
		}
		
		private TableCellEditor cellEditor()
		{
			final JTextField textField = new JTextField();
			textField.setBorder(null);
			
			
			final DefaultCellEditor editor = new DefaultCellEditor(textField)
			{
				@Override
				public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
						int column)
				{
					JTextField tf = (JTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
					RuleDefinition rule = (RuleDefinition)value;
					
					tf.setText(rule.text());
					return tf;
				}

				@Override
				public Object getCellEditorValue()
				{
					String theValue = (String)super.getCellEditorValue();
					return new UserEnteredRuleDefinition(theValue);
				}
			};
			
			return editor;
		}

		private TableCellRenderer cellRenderer()
		{
			return new DefaultTableCellRenderer()
			{
				
				@Override
				public void setValue(Object value)
				{
					RuleDefinition renderable = (RuleDefinition) value;
					//renderable.render(new Resources(), new LabelRenderTargetAdapter(this));
					setText(renderable.text());
				}
			};
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
		JTable table = new RulesTable(ignoreTable);
			
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

	private void onCancelClicked()
	{
		setVisible(false);
		dispose();
	}

	private void onOkClicked()
	{
		_okClicked = true;
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

	public boolean editProperties()
	{
		_okClicked = false;
		
		setVisible(true);
		
		System.out.println(_okClicked);
		
		return _okClicked;
	}
}
