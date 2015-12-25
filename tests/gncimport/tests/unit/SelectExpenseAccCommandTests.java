package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.models.AccountData;
import gncimport.ui.CandidateAccList;
import gncimport.ui.SelectExpenseAccCommand;
import gncimport.ui.SelectExpenseAccEvent;
import gncimport.ui.TxView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class SelectExpenseAccCommandTests
{

	private TxView _view;
	private AccSelectionInteractor _interactor;
	private SelectExpenseAccCommand _cmd;
	
	@Before
	public void Setup()
	{
		_view = mock(TxView.class);
		_interactor = mock(AccSelectionInteractor.class);
		_cmd = new SelectExpenseAccCommand(_view, _interactor);
	}

	@Test
	public void sets_selected_expense_account_for_transaction()
	{
		AccountData originalAcc = new AccountData("Original", "id-1");
		AccountData newAcc = new AccountData("New Account", "id-2");
		
		_cmd.execute(new SelectExpenseAccEvent(newAcc, originalAcc));
		
		verify(_view).selectExpenseAccForTx(newAcc);
	}
	
	@Test
	public void displays_account_tree_when_selecting_OTHER()
	{
		AccountData originalAcc = new AccountData("Original", "id-1");

		_cmd.execute(new SelectExpenseAccEvent(CandidateAccList.OTHER_ACC_PLACEHOLDER, originalAcc));
		
		verify(_interactor).browseAccounts();
	}
	
	@Test
	public void returns_account_selected_when_browsing_for_OTHER_account()
	{
		AccountData originalAcc = new AccountData("Original", "id-1");
		AccountData selectedAcc = new AccountData("New Account", "id-2");

		when(_interactor.browseAccounts()).thenReturn(selectedAcc);
		
		_cmd.execute(new SelectExpenseAccEvent(CandidateAccList.OTHER_ACC_PLACEHOLDER, originalAcc));
		
		verify(_view).selectExpenseAccForTx(selectedAcc);
	}

	@Test
	public void keeps_target_account_when_no_selection_is_made_when_browsing_for_OTHER_account()
	{
		AccountData originalAcc = new AccountData("Original", "id-1");

		when(_interactor.browseAccounts()).thenReturn(null);
		
		_cmd.execute(new SelectExpenseAccEvent(CandidateAccList.OTHER_ACC_PLACEHOLDER, originalAcc));
		
		verify(_view).selectExpenseAccForTx(originalAcc);
	}
	
	@Test
	public void still_selects_original_account_if_interactor_blows_up()
	{
		_interactor = makeBomb(AccSelectionInteractor.class, new RuntimeException("ahhhhh"));
		
		_cmd = new SelectExpenseAccCommand(_view, _interactor);
				
		_cmd.execute(new SelectExpenseAccEvent(
				CandidateAccList.OTHER_ACC_PLACEHOLDER, new AccountData("Original", "id-1")));
		
		verify(_view).handleException(any(RuntimeException.class));
		verify(_view).selectExpenseAccForTx(new AccountData("Original", "id-1"));
	}
	
	private  <T> T makeBomb(Class<T> clazz, final RuntimeException exceptionToThrow)
	{
		return mock(clazz, new Answer<Object>()
		{
			@Override
			public Object answer(InvocationOnMock invocation)
			{
				throw exceptionToThrow;
			}
		});
	}


}
