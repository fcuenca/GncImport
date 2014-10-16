package gncimport.tests.unit;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
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
import gncimport.ui.TxTableModel;
import gncimport.ui.TxView;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.gnucash.xml.gnc.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PresenterRendersTargetAccounts
{
	@Captor
	private ArgumentCaptor<DefaultMutableTreeNode> expectedAccTree;

	@Captor
	private ArgumentCaptor<List<AccountData>> expectedAccountList;

	private TxImportModel _model;
	private MainWindowPresenter _presenter;
	private TxView _view;

	@Before
	public void SetUp()
	{
		_model = mock(TxImportModel.class);
		_view = mock(TxView.class);
		_presenter = new MainWindowPresenter(_model, _view);
	}

	@Test
	public void sets_list_of_available_target_accounts()
	{
		List<AccountData> accountList = sampleAccounts();

		when(_model.getCandidateTargetAccounts()).thenReturn(accountList);

		_presenter.onReadFromCsvFile("/path/to/file.csv");

		verify(_view).displayTxData(any(TxTableModel.class), expectedAccountList.capture());

		assertThat(expectedAccountList.getValue(), hasItems(asArray(accountList)));
	}

	@Test
	public void inserts_special_place_holder_for_OTHER_accounts()
	{
		List<AccountData> accountList = sampleAccounts();

		when(_model.getCandidateTargetAccounts()).thenReturn(accountList);

		_presenter.onReadFromCsvFile("/path/to/file.csv");

		verify(_view).displayTxData(any(TxTableModel.class), expectedAccountList.capture());

		assertThat(expectedAccountList.getValue(), hasSize(accountList.size() + 1));
		assertThat(expectedAccountList.getValue(), hasItem(MainWindowPresenter.OTHER_ACC_PLACEHOLDER));
	}

	@Test
	public void renders_the_target_account_tree()
	{
		List<Account> accountList = SampleAccountData.testAccountList();

		when(_model.getAccounts()).thenReturn(accountList);

		_presenter.onSelectTargetHierarchy();

		verify(_view).displayAccountTree(expectedAccTree.capture());

		assertThat(expectedAccTree.getValue().toString(), is("Root Account"));
		assertThat(expectedAccTree.getValue().getChildCount(), is(2));
		assertThat(expectedAccTree.getValue().getChildAt(0).getChildAt(1).toString(), is("Child 2"));
	}

	@Test
	public void resets_target_account_if_selection_is_made()
	{
		AccountData selectedAccount = new AccountData("New Acc", "acc-id");
		DefaultMutableTreeNode selectedNode = new DefaultMutableTreeNode(selectedAccount);
		List<AccountData> accountList = sampleAccounts();

		when(_model.getCandidateTargetAccounts()).thenReturn(accountList);
		when(_view.displayAccountTree(any(DefaultMutableTreeNode.class))).thenReturn(selectedNode);

		_presenter.onSelectTargetHierarchy();

		verify(_view).displayTargetHierarchy("New Acc");
		verify(_view).updateCandidateTargetAccountList(expectedAccountList.capture());
		verify(_model).setTargetHierarchy(selectedAccount);

		assertThat(expectedAccountList.getValue(), hasItems(asArray(accountList)));
		assertThat(expectedAccountList.getValue(), hasSize(accountList.size() + 1));
		assertThat(expectedAccountList.getValue(), hasItem(MainWindowPresenter.OTHER_ACC_PLACEHOLDER));

	}

	@Test
	public void keeps_target_hierarchy_if_no_selection_is_made()
	{
		when(_view.displayAccountTree(any(DefaultMutableTreeNode.class))).thenReturn(null);

		_presenter.onSelectTargetHierarchy();

		verify(_view, never()).displaySourceAccount(anyString());
	}

	@Test
	public void returns_the_target_account_when_selected_from_candidate_list()
	{
		AccountData originalAcc = new AccountData("Original", "id-1");
		AccountData newAcc = new AccountData("New Account", "id-2");

		AccountData returnedAcc = _presenter.onTargetAccountSelected(newAcc, originalAcc);

		assertThat(returnedAcc, is(newAcc));
	}

	@Test
	public void displays_account_tree_when_selecting_OTHER_as_target()
	{
		AccountData originalAcc = new AccountData("Original", "id-1");
		AccountData selectedAccount = new AccountData("New Acc", "acc-id");
		DefaultMutableTreeNode selectedNode = new DefaultMutableTreeNode(selectedAccount);
		List<Account> sampleAccounts = SampleAccountData.testAccountList();

		when(_model.getAccounts()).thenReturn(sampleAccounts);
		when(_view.displayAccountTree(expectedAccTree.capture())).thenReturn(selectedNode);

		AccountData returnedAcc = _presenter.onTargetAccountSelected(
				MainWindowPresenter.OTHER_ACC_PLACEHOLDER, originalAcc);

		assertThat(returnedAcc, is(selectedAccount));

		assertThat(expectedAccTree.getValue().toString(), is("Root Account"));
		assertThat(expectedAccTree.getValue().getChildCount(), is(2));
		assertThat(expectedAccTree.getValue().getChildAt(0).getChildAt(1).toString(), is("Child 2"));
	}

	@Test
	public void keeps_target_account_when_no_selection_is_made()
	{
		AccountData originalAcc = new AccountData("Original", "id-1");

		when(_model.getAccounts()).thenReturn(SampleAccountData.testAccountList());
		when(_view.displayAccountTree(any(DefaultMutableTreeNode.class))).thenReturn(null);

		AccountData returnedAcc = _presenter.onTargetAccountSelected(
				MainWindowPresenter.OTHER_ACC_PLACEHOLDER, originalAcc);

		assertThat(returnedAcc, is(originalAcc));
	}

	private AccountData[] asArray(List<AccountData> accountList)
	{
		return accountList.toArray(new AccountData[accountList.size()]);
	}

	private List<AccountData> sampleAccounts()
	{
		List<AccountData> accountList = new ArrayList<AccountData>();
		accountList.add(new AccountData("Groceries", "id-1"));
		accountList.add(new AccountData("Entertainment", "id-2"));
		accountList.add(new AccountData("Misc Expenses", "id-3"));

		return accountList;
	}
}
