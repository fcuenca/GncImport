package gncimport.ui;

import gncimport.models.Month;
import gncimport.ui.TxView.NewHierarchyParams;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

@SuppressWarnings("serial")
public class NewAccHierarchyDialog extends JDialog
{
	private AccountTreeView _accTreeView;
	private NewHierarchyParams _params;
	private JTextField _rootAccName;
	private JComboBox _months;

	public NewAccHierarchyDialog(Frame aFrame, String title, TreeNode rootNode)
	{
		super(aFrame, true);

		setLayout(new BorderLayout());
		setTitle(title);
		setName("NEW_HIERARCHY_DLG");
				
		Dimension minimumSize = new Dimension(100, 100);

		add(createTreeView(minimumSize, rootNode), BorderLayout.PAGE_START);
		add(createAccountNamePanel(), BorderLayout.CENTER);
		add(createButtonPanel(), BorderLayout.PAGE_END);

		setupCloseOnESCkey();
		
		pack();
	}

	@SuppressWarnings("deprecation")
	private JPanel createAccountNamePanel()
	{			
		_rootAccName = new JTextField(20);
		_rootAccName.setName("ROOT_ACC_FIELD");
		
		_months = new JComboBox(Month.allMonths());
		_months.setName("MONTHS_CB");
		_months.setSelectedIndex(new Date().getMonth());
		
		JPanel panel = new JPanel(new GridBagLayout());
		
        addComponentRowToPanel(panel, new JLabel("Root Account Name: "), _rootAccName);  
        addComponentRowToPanel(panel, new JLabel("Month: "), _months); 

		return panel;
	}

	private void addComponentRowToPanel(JPanel panel, JLabel label, JComponent component)
	{
		GridBagConstraints constraints = new GridBagConstraints();  
		constraints.insets = new Insets(2,2,2,2);  
		
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		panel.add(label, constraints);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(component, constraints);
	}
	
	private  AccountTreeView createTreeView(Dimension minimumSize, TreeNode rootNode)
	{				
		_accTreeView = new AccountTreeView(rootNode, minimumSize, new AccountTreeView.Listener()
		{
			@Override
			public void onDoubleClick(TreeNode selectedNode)
			{
				//DO NOTHING
			}
		});
		
		return _accTreeView;
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
		if(_accTreeView.selectedNode() == null)
		{
			JOptionPane.showMessageDialog(this, "Please select an Account to create the Hierarchy under!");
			_accTreeView.requestFocus();
			return;
		}
		if (_rootAccName.getText().trim().isEmpty())
		{
			JOptionPane.showMessageDialog(this, "Please enter a name for the Root Account!");
			_rootAccName.requestFocus();
			return;			
		}
		
		_params = new NewHierarchyParams();
		_params.parentNode = (DefaultMutableTreeNode) _accTreeView.selectedNode();
		_params.rootAccName = _rootAccName.getText();
		_params.month = new Month(_months.getSelectedIndex() + 1);
		
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

	public NewHierarchyParams getNewHierarchyParams()
	{
		return _params;
	}
}
