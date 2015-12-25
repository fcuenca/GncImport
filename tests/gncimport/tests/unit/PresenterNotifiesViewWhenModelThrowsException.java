package gncimport.tests.unit;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.interactors.TxImportInteractor;
import gncimport.models.AccountData;
import gncimport.models.TxImportModel;
import gncimport.tests.data.SampleTxData;
import gncimport.ui.AbstractCommand;
import gncimport.ui.CandidateAccList;
import gncimport.ui.CreateAccHierarchyCommand;
import gncimport.ui.CreateAccHierarchyEvent;
import gncimport.ui.Event;
import gncimport.ui.FilterTxListCommand;
import gncimport.ui.FilterTxListEvent;
import gncimport.ui.LoadCsvCommand;
import gncimport.ui.LoadGncCommand;
import gncimport.ui.MainWindowPresenter;
import gncimport.ui.SaveGncCommand;
import gncimport.ui.SaveGncEvent;
import gncimport.ui.SelectExpenseAccCommand;
import gncimport.ui.SelectExpenseAccEvent;
import gncimport.ui.SelectSourceAccCommand;
import gncimport.ui.SelectTargetAccCommand;
import gncimport.ui.TxTableModel;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class PresenterNotifiesViewWhenModelThrowsException
{
	private RuntimeException _expectedException;
	private MainWindowPresenter _presenter;
	private TxView _view;
	private TxImportModel _model;
	private UIConfig _config;

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

	@Before
	public void setUp()
	{
		_expectedException = new RuntimeException("ahhhhh: exception wasn't handled!");

		_model = makeBomb(TxImportModel.class, _expectedException);
		
		_config = mock(UIConfig.class);
		_view = mock(TxView.class);
		
		_presenter = new MainWindowPresenter(_model, _view, mock(UIConfig.class));
	}

	@Test
	public void DELETEME_when_loading_csv_file()
	{
		when(_view.promptForFile(anyString())).thenReturn("/some/file.csv");

		_presenter.onReadFromCsvFile();

		verify(_view).handleException(_expectedException);
	}

	@Test
	public void DELETEMETOO_when_loading_csv_file()
	{
		TxBrowseInteractor interactor = makeBomb(TxBrowseInteractor.class, _expectedException);	
		LoadCsvCommand cmd = new LoadCsvCommand(_view, _config, interactor);
		
		when(_view.promptForFile(anyString())).thenReturn("/some/path");

		cmd.execute(null);
		
		verify(_view).handleException(_expectedException);
	}


	@Test
	public void DELETEME_when_loading_gnc_file()
	{
		when(_view.promptForFile(anyString())).thenReturn("/some/path");
		
		_presenter.onLoadGncFile();

		verify(_view).handleException(_expectedException);
	}

	@Test
	public void DELETEMETOO_when_loading_gnc_file()
	{
		AccFileLoadInteractor interactor = makeBomb(AccFileLoadInteractor.class, _expectedException);	
		LoadGncCommand cmd = new LoadGncCommand(_view, _config, interactor);
		
		when(_view.promptForFile(anyString())).thenReturn("/some/path");

		cmd.execute(null);
		
		verify(_view).handleException(_expectedException);
	}

	@Test
	public void DELETEME_when_saving_gnc_file()
	{
		when(_view.getTxTableModel()).thenReturn(new TxTableModel(SampleTxData.txDataList()));

		_presenter.onSaveToGncFile("/path/to/file.gnucash");

		verify(_view).handleException(_expectedException);
	}

	@Test
	public void DELETEMETOO_when_saving_gnc_file()
	{
		TxImportInteractor interactor = makeBomb(TxImportInteractor.class, _expectedException);		
		SaveGncCommand cmd = new SaveGncCommand(_view, interactor);
		
		when(_view.getTxTableModel()).thenReturn(new TxTableModel(SampleTxData.txDataList()));
		
		cmd.execute(new SaveGncEvent("/path/to/file.gnucash"));
		
		verify(_view).handleException(_expectedException);
	}
	
	@Test
	public void DELETEME_when_selecting_source_account()
	{
		_presenter.onSelectSourceAccount();

		verify(_view).handleException(_expectedException);
	}

	@Test
	public void DELETEMETOO_when_selecting_source_account()
	{
		AccSelectionInteractor interactor = makeBomb(AccSelectionInteractor.class, _expectedException);
		SelectSourceAccCommand cmd = new SelectSourceAccCommand(_view, interactor);

		cmd.execute(null);
		
		verify(_view).handleException(_expectedException);
	}
	
	@Test
	public void DELETEME_when_selecting_target_hierarchy()
	{
		_presenter.onSelectTargetHierarchy();

		verify(_view).handleException(_expectedException);
	}

	@Test
	public void DELETEMETOO_when_selecting_target_hierarchy()
	{
		AccSelectionInteractor interactor = makeBomb(AccSelectionInteractor.class, _expectedException);
		SelectTargetAccCommand cmd = new SelectTargetAccCommand(_view, interactor);
		
		cmd.execute(null);
		
		verify(_view).handleException(_expectedException);
	}

	@Test
	public void DELETEME_when_selecting_target_account()
	{
		AccountData originalAcc = new AccountData("Original", "id-1");

		_presenter.onTargetAccountSelected(CandidateAccList.OTHER_ACC_PLACEHOLDER, originalAcc);

		verify(_view).handleException(_expectedException);
		verify(_view).selectExpenseAccForTx(originalAcc);
	}

	@Test
	public void DELETEME_when_creating_new_account_hierarchy()
	{
		_presenter.onCreateNewAccHierarchy("filename");

		verify(_view).handleException(_expectedException);
	}

	@Test
	public void DELETEMETOO_when_creating_new_account_hierarchy()
	{
		AccSelectionInteractor interactor = makeBomb(AccSelectionInteractor.class, _expectedException);
		CreateAccHierarchyCommand cmd = new CreateAccHierarchyCommand(_view, _config, interactor);
		
		cmd.execute(new CreateAccHierarchyEvent("filename"));
		
		verify(_view).handleException(_expectedException);
	}
	
	@Test
	public void DELETEMETOO_when_filtering_transactions()
	{
		TxBrowseInteractor interactor = makeBomb(TxBrowseInteractor.class, _expectedException);
		FilterTxListCommand cmd = new FilterTxListCommand(_view, interactor);
		
		cmd.execute(new FilterTxListEvent(new Date(), new Date()));
		
		verify(_view).handleException(_expectedException);
	}

	@Test
	public void DELETEME_TOO_when_filtering_transactions()
	{
		FilterTxListCommand cmd = new FilterTxListCommand(_view, null) {

			@Override
			protected void doExecute(FilterTxListEvent event)
			{
				throw _expectedException;
			}
			
		};
		
		cmd.execute(new FilterTxListEvent(new Date(), new Date()));
		
		verify(_view).handleException(_expectedException);
	}

	@Test
	public void DELETEMETOO_when_selecting_target_account()
	{
		AccSelectionInteractor interactor = makeBomb(AccSelectionInteractor.class, _expectedException);
		SelectExpenseAccCommand cmd = new SelectExpenseAccCommand(_view, interactor);
		
		assertViewIsNotifiedWhenCmdBlowsUp(
				cmd, new SelectExpenseAccEvent(
						CandidateAccList.OTHER_ACC_PLACEHOLDER, new AccountData("Original", "id-1")));
		
		verify(_view).selectExpenseAccForTx(new AccountData("Original", "id-1"));
		
		
	}
		
	@Test
	public void DELETEME_notifies_view_when_it_blows_up()
	{
		AbstractCommand<Event> cmd = new AbstractCommand<Event>(_view)
		{
			@Override
			protected void doExecute(Event args)
			{	
				throw _expectedException;
			}
		};
		
		assertViewIsNotifiedWhenCmdBlowsUp(cmd, null);
	}

	private <T extends Event> void assertViewIsNotifiedWhenCmdBlowsUp(AbstractCommand<T> cmd, T args)
	{
		cmd.execute(args);
		
		verify(_view).handleException(_expectedException);
	}

	
}
