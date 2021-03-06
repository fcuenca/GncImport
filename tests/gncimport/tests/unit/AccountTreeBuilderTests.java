package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.transfer.AccountData;
import gncimport.ui.presenters.AccountTreeBuilder;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

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
		_builder.addNodeFor(new AccountData("Root Node", "id-0", null));

		assertNodeEquals(_builder.getRoot(), "Root Node", new String[] {});
	}

	@Test
	public void single_root_with_children()
	{
		_builder.addNodeFor(new AccountData("Root Node", "id-0", null));
		_builder.addNodeFor(new AccountData("Child 1", "id-1", "id-0"));
		_builder.addNodeFor(new AccountData("Child 2", "id-2", "id-0"));

		assertNodeEquals(_builder.getRoot(), "Root Node",
				new String[] { "Child 1", "Child 2" });
	}

	@Test
	public void simple_hierarchy()
	{
		_builder.addNodeFor(new AccountData("Root Node", "id-0", null));
		_builder.addNodeFor(new AccountData("Parent", "id-1", "id-0"));
		_builder.addNodeFor(new AccountData("Child 1", "id-2", "id-1"));
		_builder.addNodeFor(new AccountData("Child 2", "id-3", "id-1"));
		_builder.addNodeFor(new AccountData("Child 3", "id-4", "id-0"));

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
		_builder.addNodeFor(new AccountData("Root Node", "id-0", null));
		_builder.addNodeFor(new AccountData("Child 1", "id-1", "id-0"));

		AccountData child1 =
				(AccountData) ((DefaultMutableTreeNode) (_builder.getRoot().getChildAt(0))).getUserObject();

		assertThat(child1.toString(), is("Child 1"));
		assertThat(child1.getId(), is("id-1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void nodes_shouldnt_be_null()
	{
		_builder.addNodeFor(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void only_one_root_node_is_expected()
	{
		_builder.addNodeFor(new AccountData("Root Node", "id-0", null));
		_builder.addNodeFor(new AccountData("Root Node", "id-1", null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void unknown_parent_is_rejected()
	{
		_builder.addNodeFor(new AccountData("Root Node", "id-0", null));
		_builder.addNodeFor(new AccountData("Child", "id-1", "non-existant-id"));
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
