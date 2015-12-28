package gncimport.ui.presenters;

import gncimport.transfer.AccountData;

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

public class AccountTreeBuilder
{
	private DefaultMutableTreeNode _rootNode;
	private Map<String, DefaultMutableTreeNode> _map = new HashMap<String, DefaultMutableTreeNode>();

	public void addNodeFor(AccountData accountData)
	{
		if (accountData == null)
		{
			throw new IllegalArgumentException("account shouldn't be null");
		}

		DefaultMutableTreeNode newNode = createNewNode(accountData);

		_map.put(accountData.getId(), newNode);
	}

	private DefaultMutableTreeNode createNewNode(AccountData accountData)
	{
		DefaultMutableTreeNode newNode;

		newNode = accountData.getParentId() == null ? createRootNode(accountData) : createChildNode(accountData);

		newNode.setUserObject(accountData);

		return newNode;
	}

	private DefaultMutableTreeNode createChildNode(AccountData accountData)
	{
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();

		DefaultMutableTreeNode parentNode = _map.get(accountData.getParentId());

		if (parentNode != null)
		{
			parentNode.add(newNode);
		}
		else
		{
			throw new IllegalArgumentException("parent cannot be found for account: " + accountData.getName());
		}

		return newNode;
	}

	private DefaultMutableTreeNode createRootNode(AccountData accountData)
	{
		if (_rootNode != null)
		{
			throw new IllegalArgumentException("tree has more than one root: " + accountData.getName());
		}

		_rootNode = new DefaultMutableTreeNode();

		return _rootNode;
	}

	public DefaultMutableTreeNode getRoot()
	{
		return _rootNode;
	}

}
