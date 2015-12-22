package gncimport.tests.unit;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import gncimport.interactors.AccSelectionInteractor;
import gncimport.models.MonthlyAccountParam;
import gncimport.ui.CreateAccHierarchyCommand;
import gncimport.ui.CreateAccHierarchyEvent;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;

import org.junit.Before;
import org.junit.Test;

public class CreateAccHierarchyCommandTests
{
	private TxView _view;
	private UIConfig _config;
	private AccSelectionInteractor _interactor;
	private CreateAccHierarchyCommand _cmd;

	@Before
	public void SetUp()
	{
		_view = mock(TxView.class);
		_config = mock(UIConfig.class);
		_interactor = mock(AccSelectionInteractor.class);
		
		_cmd = new CreateAccHierarchyCommand(_view, _config, _interactor);
	}

	@Test
	public void file_name_cannot_be_blank()
	{
		_cmd.execute(new CreateAccHierarchyEvent("  "));
		
		verify(_view).displayErrorMessage(anyString());
		verify(_interactor, never()).createNewAccountHierarchy(anyListOf(MonthlyAccountParam.class), anyString());
	}
	
	@Test
	public void file_name_cannot_be_null()
	{
		_cmd.execute(new CreateAccHierarchyEvent(null));
		
		verify(_view).displayErrorMessage(anyString());
		verify(_interactor, never()).createNewAccountHierarchy(anyListOf(MonthlyAccountParam.class), anyString());
	}
	
	@Test
	public void forwards_request_to_interactor()
	{
		List<MonthlyAccountParam> monthlyAccounts = new ArrayList<MonthlyAccountParam>();
		
		when(_config.getMonthlyAccounts()).thenReturn(monthlyAccounts);
		
		_cmd.execute(new CreateAccHierarchyEvent("the file name"));
		
		verify(_interactor).createNewAccountHierarchy(monthlyAccounts, "the file name");
	}
}
