package gncimport.ui;

import javax.swing.tree.DefaultMutableTreeNode;

import org.gnucash.xml.gnc.Account;

public class AccountTreeBuilder
{
	private DefaultMutableTreeNode _rootNode;

	public AccountTreeBuilder()
	{
	}

	public void addNode(final Account account)
	{
		DefaultMutableTreeNode newNode;

		if (account.getParent() == null)
		{
			_rootNode = new DefaultMutableTreeNode();
			newNode = _rootNode;
		}
		else
		{
			newNode = new DefaultMutableTreeNode();
			_rootNode.add(newNode);
		}

		newNode.setUserObject(new Object()
		{
			private Account _account = account;

			@Override
			public String toString()
			{
				return _account.getName();
			}

		});

	}

	public DefaultMutableTreeNode getRoot()
	{
		return _rootNode;
	}

}
