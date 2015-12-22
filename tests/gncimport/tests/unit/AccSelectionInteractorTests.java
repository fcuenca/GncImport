package gncimport.tests.unit;

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



}
