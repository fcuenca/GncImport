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
	public IgnoreRulesPanel(IgnoreRulesTableModel tableModel)
	{
		super();
		setLayout(new BorderLayout());

		_ruleTable = new RuleTable(tableModel, "IGNORE_RULES");
		
		add(new JScrollPane(_ruleTable), BorderLayout.PAGE_START);
		add(createToolBar(_ruleTable, tableModel), BorderLayout.PAGE_END);
	}

	@Override
	protected JToolBar createToolBar(final JTable theTable, final RuleTableModel tableModel)
	{
		JToolBar toolBar = super.createToolBar(theTable, tableModel);
				
		toolBar.addSeparator();
		
		JButton button = new JButton();
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
				if(((IgnoreRulesTableModel)tableModel).testRulesWithText(textField.getText()))
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
}