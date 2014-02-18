package gncimport.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

@SuppressWarnings("serial")
public class AccountTreeBrowserDialog extends JDialog
{
	private TreeNode _selectedNode;
	private JTree _tree;

	public AccountTreeBrowserDialog(Frame aFrame, String title, TreeNode rootNode)
	{
		super(aFrame, true);

		setLayout(new BorderLayout());
		setTitle(title);

		Dimension minimumSize = new Dimension(100, 100);

		add(createTreeView(minimumSize, rootNode), BorderLayout.CENTER);
		add(createButtonPanel(), BorderLayout.PAGE_END);

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
		buttonPanel.add(button);
		button.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				_selectedNode = (TreeNode) _tree.getLastSelectedPathComponent();
				setVisible(false);
				dispose();
			}
		});

		button = new JButton("Cancel");
		buttonPanel.add(button);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				_selectedNode = null;
				setVisible(false);
				dispose();
			}
		});

		return buttonPanel;
	}

	private JScrollPane createTreeView(Dimension minimumSize, TreeNode rootNode)
	{
		_tree = createTree(rootNode);

		JScrollPane treeView = new JScrollPane(_tree);
		treeView.setMinimumSize(minimumSize);
		treeView.setPreferredSize(new Dimension(500, 300));

		return treeView;
	}

	private JTree createTree(TreeNode rootNode)
	{
		JTree tree = new JTree(rootNode);
		tree.setName("TREE_CTRL");
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);

		// tree.addTreeSelectionListener(this);

		return tree;
	}

}
