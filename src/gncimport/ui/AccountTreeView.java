package gncimport.ui;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

public class AccountTreeView extends JScrollPane
{
	private static final long serialVersionUID = -5245580295335421485L;

	public interface Listener
	{
		public void onDoubleClick(TreeNode selectedNode);
	}
	
	private JTree _tree;

	public AccountTreeView(TreeNode rootNode, Dimension minimumSize, final Listener listener)
	{
		_tree = createTree(rootNode);
		
		setViewportView(_tree);

		setMinimumSize(minimumSize);
		setPreferredSize(new Dimension(500, 300));
		
		_tree.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				int selRow = _tree.getRowForLocation(e.getX(), e.getY());
				if (selRow != -1)
				{
					if (e.getClickCount() == 2)
					{
						listener.onDoubleClick(selectedNode());
					}
				}
			}
		});
	}

	public TreeNode selectedNode()
	{
		return (TreeNode) _tree.getLastSelectedPathComponent();
	}
	
	private JTree createTree(TreeNode rootNode)
	{
		final JTree tree = new JTree(rootNode);
		
		tree.setName("ACC_TREE");
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		
		return tree;
	}
}
