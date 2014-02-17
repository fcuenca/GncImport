package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.models.AccountData;
import gncimport.ui.AccountTreeBuilder;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.gnucash.xml.act.Id;
import org.gnucash.xml.act.Parent;
import org.gnucash.xml.gnc.Account;
import org.junit.Before;
import org.junit.Test;

public class AccountTreeBuilderTests
{
	private AccountTreeBuilder _builder;

	@Before
	public void setup()
	{
		_builder = new AccountTreeBuilder();
	}

	@Test
	public void single_root_node()
	{
		_builder.addNode(accountWith("Root Node", "id-0", null));

		assertNodeEquals(_builder.getRoot(), "Root Node", new String[] {});
	}

	@Test
	public void single_root_with_children()
	{
		_builder.addNode(accountWith("Root Node", "id-0", null));
		_builder.addNode(accountWith("Child 1", "id-1", "id-0"));
		_builder.addNode(accountWith("Child 2", "id-2", "id-0"));

		assertNodeEquals(_builder.getRoot(), "Root Node",
				new String[] { "Child 1", "Child 2" });
	}

	@Test
	public void simple_hierarchy()
	{
		_builder.addNode(accountWith("Root Node", "id-0", null));
		_builder.addNode(accountWith("Parent", "id-1", "id-0"));
		_builder.addNode(accountWith("Child 1", "id-2", "id-1"));
		_builder.addNode(accountWith("Child 2", "id-3", "id-1"));
		_builder.addNode(accountWith("Child 3", "id-4", "id-0"));

		DefaultMutableTreeNode root = _builder.getRoot();

		assertNodeEquals(root, "Root Node", new String[] { "Parent", "Child 3" });
		assertNodeEquals(root.getChildAt(0), "Parent", new String[] { "Child 1", "Child 2" });
		assertNodeEquals(root.getChildAt(0).getChildAt(0), "Child 1", new String[] {});
		assertNodeEquals(root.getChildAt(0).getChildAt(1), "Child 2", new String[] {});
		assertNodeEquals(root.getChildAt(1), "Child 3", new String[] {});

	}

	@Test
	public void nodes_store_account_as_payload()
	{
		_builder.addNode(accountWith("Root Node", "id-0", null));
		_builder.addNode(accountWith("Child 1", "id-1", "id-0"));

		AccountData child1 =
				(AccountData) ((DefaultMutableTreeNode) (_builder.getRoot().getChildAt(0))).getUserObject();

		assertThat(child1.toString(), is("Child 1"));
		assertThat(child1.getId(), is("id-1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void nodes_shouldnt_be_null()
	{
		_builder.addNode(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void only_one_root_node_is_expected()
	{
		_builder.addNode(accountWith("Root Node", "id-0", null));
		_builder.addNode(accountWith("Root Node", "id-1", null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void unknown_parent_is_rejected()
	{
		_builder.addNode(accountWith("Root Node", "id-0", null));
		_builder.addNode(accountWith("Child", "id-1", "non-existant-id"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void acccounts_should_have_an_id()
	{
		_builder.addNode(accountWith("Root Node", null, null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void acount_ids_shouldnt_be_blank()
	{
		_builder.addNode(accountWith("Root Node", "", null));
	}

	private Account accountWith(String accName, String accId, String parentAccId)
	{
		Account account = new Account();
		account.setName(accName);

		if (accId != null)
		{
			Id id = new Id();
			id.setValue(accId);
			account.setId(id);
		}

		if (parentAccId != null)
		{
			Parent parent = new Parent();
			parent.setValue(parentAccId);
			account.setParent(parent);
		}

		return account;
	}

	private void assertNodeEquals(TreeNode node, String nodeName, String[] expectedChildren)
	{
		assertThat(node.toString(), is(nodeName));
		assertThat(node.getChildCount(), is(expectedChildren.length));

		for (int i = 0; i < expectedChildren.length; i++)
		{
			assertThat("child node at: " + i, node.getChildAt(i).toString(), is(expectedChildren[i]));
		}
	}

}
