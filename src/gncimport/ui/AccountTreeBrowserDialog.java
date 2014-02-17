package gncimport.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

@SuppressWarnings("serial")
public class AccountTreeBrowserDialog extends JDialog
{
	public AccountTreeBrowserDialog(Frame aFrame, String title, DefaultMutableTreeNode rootNode)
	{
		super(aFrame, true);

		setLayout(new BorderLayout());

		setTitle(title);

		Dimension minimumSize = new Dimension(100, 100);

		JScrollPane scrolledTreeView = createTreeView(minimumSize, rootNode);

		add(scrolledTreeView, BorderLayout.CENTER);

		pack();
	}

	private JScrollPane createTreeView(Dimension minimumSize, DefaultMutableTreeNode rootNode)
	{
		JTree tree = createTree(rootNode);

		JScrollPane treeView = new JScrollPane(tree);
		treeView.setMinimumSize(minimumSize);
		treeView.setPreferredSize(new Dimension(500, 300));

		return treeView;
	}

	private JTree createTree(DefaultMutableTreeNode rootNode)
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
