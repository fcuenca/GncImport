package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.ui.AccountTreeBuilder;

import javax.swing.tree.DefaultMutableTreeNode;

import org.gnucash.xml.act.Id;
import org.gnucash.xml.act.Parent;
import org.gnucash.xml.gnc.Account;
import org.junit.Test;

public class AccountTreeBuilderTests
{
	@Test
	public void single_root_node()
	{
		AccountTreeBuilder builder = new AccountTreeBuilder();

		builder.addNode(accountWith("Root Node", "id-0", null));

		DefaultMutableTreeNode root = builder.getRoot();

		assertThat(root.toString(), is("Root Node"));
	}

	@Test
	public void single_root_with_children()
	{
		AccountTreeBuilder builder = new AccountTreeBuilder();

		builder.addNode(accountWith("Root Node", "id-0", null));
		builder.addNode(accountWith("Child 1", "id-1", "id-0"));
		builder.addNode(accountWith("Child 2", "id-2", "id-0"));

		DefaultMutableTreeNode root = builder.getRoot();

		assertThat(root.toString(), is("Root Node"));
		assertThat(root.getChildCount(), is(2));
		assertThat(root.getChildAt(0).toString(), is("Child 1"));
		assertThat(root.getChildAt(1).toString(), is("Child 2"));
	}

	// TODO: @Test
	public void simple_hierarchy()
	{
		AccountTreeBuilder builder = new AccountTreeBuilder();

		builder.addNode(accountWith("Root Node", "id-0", null));
		builder.addNode(accountWith("Parent", "id-1", "id-0"));
		builder.addNode(accountWith("Child", "id-2", "id-1"));

		DefaultMutableTreeNode root = builder.getRoot();

		assertThat(root.toString(), is("Root Node"));
		assertThat(root.getChildCount(), is(1));
		assertThat(root.getChildAt(0).toString(), is("Child 1"));
		assertThat(root.getChildAt(0).getChildCount(), is(1));
		assertThat(root.getChildAt(0).getChildAt(0).toString(), is("Child"));
	}

	private Account accountWith(String accName, String accId, String parentAccId)
	{
		Id id = new Id();
		id.setValue(accId);

		Account account = new Account();
		account.setName(accName);
		account.setId(id);

		if (parentAccId != null)
		{
			Parent parent = new Parent();
			parent.setValue(parentAccId);
			account.setParent(parent);
		}

		return account;
	}
}
