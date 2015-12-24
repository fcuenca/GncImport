package gncimport.tests.unit;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.AccSelectionInteractor.NewHierarchyOpts;
import gncimport.models.AccountData;
import gncimport.models.Month;
import gncimport.models.MonthlyAccountParam;
import gncimport.models.TxImportModel;
import gncimport.tests.data.SampleAccountData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AccSelectionInteractorTests
{

	private AccSelectionInteractor.OutPort _outPort;
	private TxImportModel _model;
	private AccSelectionInteractor _interactor;

	@Before
	public void SetUp()
	{
		_outPort = mock(AccSelectionInteractor.OutPort.class);
		_model = mock(TxImportModel.class);

		_interactor = new AccSelectionInteractor(_outPort, _model);
	}
	
	@Test
	public void prompts_user_to_select_an_account()
	{
		AccountData selectedAccount = new AccountData("Account", "id");
		List<AccountData> accountList = SampleAccountData.testAccountList();

		when(_model.getAccounts()).thenReturn(accountList);
		when(_outPort.selectAccount(accountList)).thenReturn(selectedAccount);

		assertThat(_interactor.browseAccounts(), is(selectedAccount));
	}

	@Test
	public void prompts_user_to_select_target_account()
	{
		List<AccountData> accountList = SampleAccountData.testAccountList();

		when(_model.getAccounts()).thenReturn(accountList);

		_interactor.selectTargetAccount();
		
		verify(_outPort).selectAccount(accountList);
	}
	
	@Test
	public void prompts_user_to_select_source_account()
	{
		List<AccountData> accountList = SampleAccountData.testAccountList();
		
		when(_model.getAccounts()).thenReturn(accountList);
		
		_interactor.selectSourceAccount();
		
		verify(_outPort).selectAccount(accountList);
	}
	
	@Test
	public void resets_target_account_if_selection_is_made()
	{
		AccountData selectedAccount = new AccountData("selected Account", "id");
		List<AccountData> accountList = sampleAccounts();
		
		when(_outPort.selectAccount(anyListOf(AccountData.class))).thenReturn(selectedAccount);
		when(_model.getCandidateTargetAccounts()).thenReturn(accountList);

		_interactor.selectTargetAccount();
		
		verify(_model).setTargetHierarchy(selectedAccount);
		verify(_outPort).targetHierarchyHasBeenSet("selected Account", accountList);
	}
	
	@Test
	public void resets_source_account_if_selection_is_made()
	{
		AccountData selectedAccount = new AccountData("selected Account", "id");
		List<AccountData> accountList = sampleAccounts();
		
		when(_outPort.selectAccount(anyListOf(AccountData.class))).thenReturn(selectedAccount);
		when(_model.getCandidateTargetAccounts()).thenReturn(accountList);
		
		_interactor.selectSourceAccount();
		
		verify(_model).setSourceAccount(selectedAccount);
		verify(_outPort).sourceAccHasBeenSet("selected Account");
	}
	
	@Test
	public void keeps_target_hierarchy_if_no_selection_is_made()
	{
		when(_outPort.selectAccount(anyListOf(AccountData.class))).thenReturn(null);

		_interactor.selectTargetAccount();

		verify(_model, never()).setTargetHierarchy(any(AccountData.class));
		verify(_outPort, never()).targetHierarchyHasBeenSet(anyString(), anyListOf(AccountData.class));
	}

	@Test
	public void keeps_source_hierarchy_if_no_selection_is_made()
	{
		when(_outPort.selectAccount(anyListOf(AccountData.class))).thenReturn(null);
		
		_interactor.selectSourceAccount();
		
		verify(_model, never()).setSourceAccount(any(AccountData.class));
		verify(_outPort, never()).sourceAccHasBeenSet(anyString());
	}
	
	@Test
	public void creates_new_account_hierarchy()
	{
		List<AccountData> accountList = SampleAccountData.testAccountList();
		
		List<MonthlyAccountParam> standardMonthlyAccounts = new ArrayList<MonthlyAccountParam>();	
		standardMonthlyAccounts.add(new MonthlyAccountParam(1, "some account"));
		
		NewHierarchyOpts options = new NewHierarchyOpts(new AccountData("account", "acc-id"), "New Hierarchy Root", new Month("January"));
		
		when(_model.getAccounts()).thenReturn(accountList);		
		when(_outPort.promptForNewHierarchy(accountList)).thenReturn(options);

		_interactor.createNewAccountHierarchy(standardMonthlyAccounts, "/the/file/name");
		
		verify(_model).createNewAccountHierarchy(options.parentAcc, options.hierarchyRoot, options.month, standardMonthlyAccounts, "/the/file/name");
	}
	
	@Test
	public void hierarchy_not_created_when_selection_is_aborted()
	{
		List<AccountData> accountList = SampleAccountData.testAccountList();
		List<MonthlyAccountParam> standardMonthlyAccounts = new ArrayList<MonthlyAccountParam>();	

		when(_model.getAccounts()).thenReturn(accountList);		
		when(_outPort.promptForNewHierarchy(accountList)).thenReturn(null);

		_interactor.createNewAccountHierarchy(standardMonthlyAccounts, "/the/file/name");
		
		verify(_model, never()).createNewAccountHierarchy(
				any(AccountData.class), anyString(), any(Month.class), anyListOf(MonthlyAccountParam.class), anyString());
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
