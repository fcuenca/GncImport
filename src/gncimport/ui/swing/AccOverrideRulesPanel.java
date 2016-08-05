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
public class AccOverrideRulesPanel extends PropertyEditorPanel
{
	public AccOverrideRulesPanel(AccOverrideRulesTableModel tableModel)
	{
		super();
		setLayout(new BorderLayout());
		
		_ruleTable = new RuleTable(tableModel, "ACC_OVERRIDE_RULES");
		_ruleTable.setName("ACC_OVERRIDE_RULES");
		
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
				String result = ((AccOverrideRulesTableModel)tableModel).testRulesWithText(textField.getText());
				
				if(result != "")
				{
					resultLabel.setText(result);
					resultLabel.setIcon(passIcon);
				}
				else
				{
					resultLabel.setText(null);
					resultLabel.setIcon(failIcon);
				}
			}
		});
		
		textField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				resultLabel.setText(null);
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