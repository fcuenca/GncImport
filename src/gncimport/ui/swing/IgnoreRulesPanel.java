package gncimport.ui.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class IgnoreRulesPanel extends PropertyEditorPanel
{
	JTable _ignoreTable;

	public IgnoreRulesPanel(IgnoreRulesTableModel ignoreTblModel)
	{
		super();
		setLayout(new BorderLayout());

		_ignoreTable = new RuleTable(ignoreTblModel);
		
		add(new JScrollPane(_ignoreTable), BorderLayout.PAGE_START);
		add(createToolBar(_ignoreTable, ignoreTblModel), BorderLayout.PAGE_END);
	}

	JToolBar createToolBar(final JTable theTable, final IgnoreRulesTableModel ignoreTblModel)
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
				ignoreTblModel.newRow();
			}
		});
	
		button = new JButton("-");
		button.setToolTipText("Remove selected Rule");
		toolBar.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ignoreTblModel.removeRule(theTable.getSelectedRow());
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
				if(ignoreTblModel.testRulesWithText(textField.getText()))
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
	
	@Override
	public void stopEditing()
	{
		if (_ignoreTable.getCellEditor() != null) 
		{
		      _ignoreTable.getCellEditor().stopCellEditing();
		}
	}


}