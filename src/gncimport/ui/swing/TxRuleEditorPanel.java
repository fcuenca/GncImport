package gncimport.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class TxRuleEditorPanel extends PropertyEditorPanel
{
	public TxRuleEditorPanel(TxRuleTableModel model, String name)
	{
		super(new ScreenValueTable(model, name));
	}

	protected void addPanelSpecificToolbarControls(JToolBar toolBar)
	{
		JButton button;
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
				String result = testStringAgainstRules(textField.getText());

				resultLabel.setText(result);
				
				if(result != null)
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
				resultLabel.setText(null);
				resultLabel.setIcon(null);
				super.keyPressed(e);
			}
		});
	
		toolBar.add(button);
		toolBar.add(textField);
		toolBar.add(resultLabel);
	}
	
	private String testStringAgainstRules(String testString)
	{
		return ((TxRuleTableModel) _propertyTable.getModel()).testRulesWithText(testString);
	}

}