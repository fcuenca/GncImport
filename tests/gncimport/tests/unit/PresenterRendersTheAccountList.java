package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.models.AccountData;
import gncimport.models.TxImportModel;
import gncimport.tests.data.SampleAccountData;
import gncimport.ui.MainWindowPresenter;
import gncimport.ui.TxView;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PresenterRendersTheAccountList
{
	@Captor
	private ArgumentCaptor<DefaultMutableTreeNode> expectedAccTree;

	private MainWindowPresenter _presenter;
	private TxView _view;
	private TxImportModel _model;

	@Before
	public void SetUp()
	{
		_model = mock(TxImportModel.class);
		_view = mock(TxView.class);
		_presenter = new MainWindowPresenter(_model, _view);
	}

	@Test
	public void initilizes_gnc_file()
	{
		_presenter.onLoadGncFile("/path/to/gnc.xml");

		verify(_model).openGncFile("/path/to/gnc.xml");
	}

	@Test
	public void renders_the_source_account_tree()
	{
		List<AccountData> accountList = SampleAccountData.testAccountList();

		when(_model.getAccounts()).thenReturn(accountList);

		_presenter.onSelectSourceAccount();

		verify(_view).displayAccountTree(expectedAccTree.capture());

		assertThat(expectedAccTree.getValue().toString(), is("Root Account"));
		assertThat(expectedAccTree.getValue().getChildCount(), is(2));
		assertThat(expectedAccTree.getValue().getChildAt(0).getChildAt(1).toString(), is("Child 2"));
	}

	@Test
	public void keeps_source_account_if_no_selection_is_made()
	{
		when(_view.displayAccountTree(any(DefaultMutableTreeNode.class))).thenReturn(null);

		_presenter.onSelectSourceAccount();

		verify(_view, never()).displaySourceAccount(anyString());
	}

	@Test
	public void resets_source_account_if_selection_is_made()
	{
		AccountData selectedAccount = new AccountData("New Acc", "acc-id");
		DefaultMutableTreeNode selectedNode = new DefaultMutableTreeNode(selectedAccount);

		when(_view.displayAccountTree(any(DefaultMutableTreeNode.class))).thenReturn(selectedNode);

		_presenter.onSelectSourceAccount();

		verify(_view).displaySourceAccount("New Acc");
		verify(_model).setSourceAccount(selectedAccount);
	}

}
