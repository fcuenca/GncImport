package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.models.AccountData;
import gncimport.models.Month;
import gncimport.models.MonthlyAccountParam;
import gncimport.models.TxImportModel;
import gncimport.tests.data.SampleAccountData;
import gncimport.ui.MainWindowPresenter;
import gncimport.ui.TxView;
import gncimport.ui.TxView.NewHierarchyParams;
import gncimport.ui.UIConfig;
import gncimport.utils.ProgrammerError;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PresenterCreatesNewAccountHierarchies
{
	@Captor
	private ArgumentCaptor<DefaultMutableTreeNode> expectedAccTree;

	private MainWindowPresenter _presenter;
	private TxView _view;
	private TxImportModel _model;
	private UIConfig _config;

	@Before
	public void SetUp()
	{
		_model = mock(TxImportModel.class);
		_view = mock(TxView.class);
		_config = mock(UIConfig.class);
		
		_presenter = new MainWindowPresenter(_model, _view, _config);
	}
	
	@Test
	public void allowed_only_after_opening_gnc_file()
	{
		_presenter.onCreateNewAccHierarchy("");
		
		verify(_view).displayErrorMessage(anyString());
		verify(_model, never()).createNewAccountHierarchy(
				any(AccountData.class), anyString(), any(Month.class), anyListOf(MonthlyAccountParam.class), anyString());
	}

	@Test
	public void renders_the_account_tree()
	{
		List<AccountData> accountList = SampleAccountData.testAccountList();

		when(_model.getAccounts()).thenReturn(accountList);

		_presenter.onCreateNewAccHierarchy("filename");

		verify(_view).promptForNewHierarchy(expectedAccTree.capture());

		assertThat(expectedAccTree.getValue().toString(), is("Root Account"));
		assertThat(expectedAccTree.getValue().getChildCount(), is(2));
		assertThat(expectedAccTree.getValue().getChildAt(0).getChildAt(1).toString(), is("Child 2"));
	}
	
	@Test
	public void aborts_if_no_selection_is_made()
	{
		when(_view.promptForNewHierarchy(any(DefaultMutableTreeNode.class))).thenReturn(null);

		_presenter.onCreateNewAccHierarchy("filename");

		verify(_model, never()).createNewAccountHierarchy(
				any(AccountData.class), anyString(), any(Month.class), anyListOf(MonthlyAccountParam.class), anyString());
	}
	
	@Test
	public void parent_account_cannot_be_null()
	{
		NewHierarchyParams hierarchyParams = new  NewHierarchyParams(); 
		hierarchyParams.parentNode = null;
		hierarchyParams.rootAccName = "New Hierarchy Root";
		
		when(_view.promptForNewHierarchy(any(DefaultMutableTreeNode.class))).thenReturn(hierarchyParams);

		_presenter.onCreateNewAccHierarchy("filename");
		
		verify(_view).handleException(any(ProgrammerError.class));
	}
	
	@Test
	public void root_acc_name_cannot_be_null()
	{
		NewHierarchyParams hierarchyParams = new  NewHierarchyParams(); 
		hierarchyParams.parentNode = new DefaultMutableTreeNode();
		hierarchyParams.rootAccName = null;
		
		when(_view.promptForNewHierarchy(any(DefaultMutableTreeNode.class))).thenReturn(hierarchyParams);

		_presenter.onCreateNewAccHierarchy("filename");
		
		verify(_view).handleException(any(ProgrammerError.class));
	}
	
	@Test
	public void root_acc_name_cannot_be_blank()
	{
		NewHierarchyParams hierarchyParams = new  NewHierarchyParams(); 
		hierarchyParams.parentNode = new DefaultMutableTreeNode();
		hierarchyParams.rootAccName = "   ";
		
		when(_view.promptForNewHierarchy(any(DefaultMutableTreeNode.class))).thenReturn(hierarchyParams);

		_presenter.onCreateNewAccHierarchy("filename");
		
		verify(_view).handleException(any(ProgrammerError.class));
	}
	
	@Test
	public void triggers_account_creation_when_selection_is_made()
	{
		AccountData selectedAccount = new AccountData("Parent Account", "acc-id");
		
		NewHierarchyParams hierarchyParams = new  NewHierarchyParams(); 
		hierarchyParams.parentNode = new DefaultMutableTreeNode(selectedAccount);
		hierarchyParams.rootAccName  ="New Hierarchy Root";
		hierarchyParams.month = new Month(10);
		
		List<MonthlyAccountParam> subAccList = new ArrayList<MonthlyAccountParam>();
		
		when(_config.getMonthlyAccounts()).thenReturn(subAccList);
		when(_view.promptForNewHierarchy(any(DefaultMutableTreeNode.class))).thenReturn(hierarchyParams);

		_presenter.onCreateNewAccHierarchy("filename");

		verify(_model).createNewAccountHierarchy(selectedAccount, "New Hierarchy Root", new Month(10),
				subAccList, "filename");
	}

}
