package gncimport.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
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
				closeWithSelection((TreeNode) _tree.getLastSelectedPathComponent());
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
		final JTree tree = new JTree(rootNode);
		
		tree.setName("ACC_TREE");
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		
		tree.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				if (selRow != -1)
				{
					if (e.getClickCount() == 2)
					{
						onDoubleClick(selPath);
					}
				}
			}
		});

		return tree;
	}

	private void closeWithSelection(TreeNode treeNode)
	{
		_selectedNode = treeNode;
		setVisible(false);
		dispose();
	}
	
	private void onDoubleClick(TreePath selPath)
	{
		TreeNode node = (TreeNode) _tree.getLastSelectedPathComponent();

		if (node.isLeaf())
		{
			closeWithSelection(node);
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
