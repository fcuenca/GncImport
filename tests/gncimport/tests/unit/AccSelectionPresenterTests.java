package gncimport.tests.unit;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.interactors.AccSelectionInteractor.NewHierarchyOpts;
import gncimport.models.AccountData;
import gncimport.models.Month;
import gncimport.tests.data.SampleAccountData;
import gncimport.ui.AccSelectionPresenter;
import gncimport.ui.TxView;
import gncimport.ui.TxView.NewHierarchyParams;
import gncimport.utils.ProgrammerError;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccSelectionPresenterTests
{
	@Captor
	private ArgumentCaptor<DefaultMutableTreeNode> expectedAccTree;

	private TxView _view;
	private AccSelectionPresenter _presenter;
	
	List<AccountData> accountList = SampleAccountData.testAccountList();

	@Before
	public void SetUp()
	{
		_view = mock(TxView.class);
		_presenter = new AccSelectionPresenter(_view);
	}

	@Test
	public void renders_the_account_tree()
	{		
		_presenter.promptForNewHierarchy(accountList);
		
		verify(_view).promptForNewHierarchy(expectedAccTree.capture());

		assertThat(expectedAccTree.getValue().toString(), is("Root Account"));
		assertThat(expectedAccTree.getValue().getChildCount(), is(2));
		assertThat(expectedAccTree.getValue().getChildAt(0).getChildAt(1).toString(), is("Child 2"));
	}
	
	@Test
	public void returns_selected_account_for_new_hierarchy()
	{
		AccountData selectedAccount = new AccountData("Parent Account", "acc-id");

		NewHierarchyParams hierarchyParams = new  NewHierarchyParams(); 
		hierarchyParams.parentNode = new DefaultMutableTreeNode(selectedAccount);
		hierarchyParams.rootAccName  ="New Hierarchy Root";
		hierarchyParams.month = new Month(10);

		when(_view.promptForNewHierarchy(any(DefaultMutableTreeNode.class))).thenReturn(hierarchyParams);
		
		NewHierarchyOpts options = _presenter.promptForNewHierarchy(accountList);
		
		assertThat(options.hierarchyRoot, is(hierarchyParams.rootAccName));
		assertThat(options.month, is(hierarchyParams.month));
		assertThat(options.parentAcc, is(selectedAccount));
	}

	@Test
	public void returns_null_when_selection_is_canceled()
	{
		when(_view.promptForNewHierarchy(any(DefaultMutableTreeNode.class))).thenReturn(null);

		assertThat(_presenter.promptForNewHierarchy(accountList), is(nullValue()));
	}

	@Test(expected=ProgrammerError.class)
	public void parent_account_cannot_be_null()
	{
		NewHierarchyParams hierarchyParams = new  NewHierarchyParams(); 
		hierarchyParams.parentNode = null;
		hierarchyParams.rootAccName = "New Hierarchy Root";
		
		when(_view.promptForNewHierarchy(any(DefaultMutableTreeNode.class))).thenReturn(hierarchyParams);

		_presenter.promptForNewHierarchy(accountList);
	}
	
	@Test(expected=ProgrammerError.class)
	public void root_acc_name_cannot_be_null()
	{
		NewHierarchyParams hierarchyParams = new  NewHierarchyParams(); 
		hierarchyParams.parentNode = new DefaultMutableTreeNode();
		hierarchyParams.rootAccName = null;
		
		when(_view.promptForNewHierarchy(any(DefaultMutableTreeNode.class))).thenReturn(hierarchyParams);

		_presenter.promptForNewHierarchy(accountList);
	}
	
	@Test(expected=ProgrammerError.class)
	public void root_acc_name_cannot_be_blank()
	{
		NewHierarchyParams hierarchyParams = new  NewHierarchyParams(); 
		hierarchyParams.parentNode = new DefaultMutableTreeNode();
		hierarchyParams.rootAccName = "   ";
		
		when(_view.promptForNewHierarchy(any(DefaultMutableTreeNode.class))).thenReturn(hierarchyParams);

		_presenter.promptForNewHierarchy(accountList);
	}
}
