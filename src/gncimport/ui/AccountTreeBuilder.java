package gncimport.ui;

import gncimport.models.AccountData;

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.gnucash.xml.act.Id;
import org.gnucash.xml.gnc.Account;

public class AccountTreeBuilder
{
	private DefaultMutableTreeNode _rootNode;
	private Map<String, DefaultMutableTreeNode> _map = new HashMap<String, DefaultMutableTreeNode>();

	public void addNodeFor(final Account account)
	{
		checkAccountIsValid(account);

		DefaultMutableTreeNode newNode = createNewNode(account);

		_map.put(account.getId().getValue(), newNode);
	}

	private DefaultMutableTreeNode createNewNode(final Account account)
	{
		DefaultMutableTreeNode newNode;

		newNode = account.getParent() == null ? createRootNode(account) : createChildNode(account);

		newNode.setUserObject(new AccountData(account.getName(), account.getId().getValue()));

		return newNode;
	}

	private DefaultMutableTreeNode createChildNode(final Account account)
	{
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();

		DefaultMutableTreeNode parentNode = _map.get(account.getParent().getValue());

		if (parentNode != null)
		{
			parentNode.add(newNode);
		}
		else
		{
			throw new IllegalArgumentException("parent cannot be found for account: " + account.getName());
		}

		return newNode;
	}

	private DefaultMutableTreeNode createRootNode(final Account account)
	{
		if (_rootNode != null)
		{
			throw new IllegalArgumentException("tree has more than one root: " + account.getName());
		}

		_rootNode = new DefaultMutableTreeNode();

		return _rootNode;
	}

	private void checkAccountIsValid(final Account account)
	{
		if (account == null)
		{
			throw new IllegalArgumentException("account shouldn't be null");
		}
		else
		{
			Id id = account.getId();

			if (id != null)
			{
				if (id.getValue() == null || id.getValue().isEmpty())
				{
					throw new IllegalArgumentException("Account ID value is null or empty for: " + account.getName());
				}
			}
			else
			{
				throw new IllegalArgumentException("Account ID is null for: " + account.getName());
			}
		}
	}

	public DefaultMutableTreeNode getRoot()
	{
		return _rootNode;
	}

}
