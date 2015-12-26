package gncimport.ui.swing;

import gncimport.ui.swing.AccountTreeView.Listener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.tree.TreeNode;

@SuppressWarnings("serial")
public class AccountTreeBrowserDialog extends JDialog
{
	private TreeNode _selectedNode;
	private AccountTreeView _accTreeView;

	public AccountTreeBrowserDialog(Frame aFrame, String title, TreeNode rootNode)
	{
		super(aFrame, true);

		setLayout(new BorderLayout());
		setTitle(title);
		setName("ACC_SELECTION_DLG");

		Dimension minimumSize = new Dimension(100, 100);
		
		add(createTreeView(minimumSize, rootNode), BorderLayout.CENTER);
		add(createButtonPanel(), BorderLayout.PAGE_END);

		setupCloseOnESCkey();
		
		pack();
	}

	public TreeNode getSelectedNode()
	{
		return _selectedNode;
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
				closeWithSelection(_accTreeView.selectedNode());
			}
		});

		button = new JButton("Cancel");
		button.setName("CANCEL_BUTTON");
		buttonPanel.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				closeWithSelection(null);
			}
		});

		return buttonPanel;
	}

	private  AccountTreeView createTreeView(Dimension minimumSize, TreeNode rootNode)
	{				
		final AccountTreeView.Listener listener = new AccountTreeView.Listener()
		{
			@Override
			public void onDoubleClick(TreeNode selectedNode)
			{
				closeIfLeafSelected(selectedNode);
			}
		};
		
		_accTreeView = new AccountTreeView(rootNode, minimumSize, listener);
		
		return _accTreeView;
	}

	private void closeWithSelection(TreeNode treeNode)
	{
		_selectedNode = treeNode;
		setVisible(false);
		dispose();
	}
	
	private void closeIfLeafSelected(TreeNode selectedNode)
	{
		if (selectedNode.isLeaf())
		{
			closeWithSelection(selectedNode);
		}
	}
	
	public void setupCloseOnESCkey()
	{
		ActionListener escListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				closeWithSelection(null);
			}
		};

		getRootPane().registerKeyboardAction(escListener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}
}
