package gncimport.tests.unit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;
import gncimport.models.AccountData;
import gncimport.models.TxImportModel;
import gncimport.tests.data.SampleTxData;
import gncimport.ui.CandidateAccList;
import gncimport.ui.MainWindowPresenter;
import gncimport.ui.TxTableModel;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;

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

	@Before
	public void setUp()
	{
		_expectedException = new RuntimeException("ahhhhh: exception wasn't handled!");

		_model = mock(TxImportModel.class, new Answer<Object>()
		{
			@Override
			public Object answer(InvocationOnMock invocation)
			{
				throw _expectedException;
			}
		});

		_view = mock(TxView.class);
		_presenter = new MainWindowPresenter(_model, _view, mock(UIConfig.class));
	}

	@Test
	public void when_loading_csv_file()
	{
		when(_view.promptForFile(anyString())).thenReturn("/some/file.csv");

		_presenter.onReadFromCsvFile();

		verify(_view).handleException(_expectedException);
	}

	@Test
	public void when_loading_gnc_file()
	{
		when(_view.promptForFile(anyString())).thenReturn("/some/path");
		
		_presenter.onLoadGncFile();

		verify(_view).handleException(_expectedException);
	}

	@Test
	public void when_saving_gnc_file()
	{
		when(_view.getTxTableModel()).thenReturn(new TxTableModel(SampleTxData.txDataList()));

		_presenter.onSaveToGncFile("/path/to/file.gnucash");

		verify(_view).handleException(_expectedException);
	}

	@Test
	public void when_selecting_source_account()
	{
		_presenter.onSelectSourceAccount();

		verify(_view).handleException(_expectedException);
	}

	@Test
	public void when_selecting_target_hierarchy()
	{
		_presenter.onSelectTargetHierarchy();

		verify(_view).handleException(_expectedException);
	}

	@Test
	public void when_selecting_target_account()
	{
		AccountData originalAcc = new AccountData("Original", "id-1");

		AccountData selectedAcc = _presenter.onTargetAccountSelected(
				CandidateAccList.OTHER_ACC_PLACEHOLDER, originalAcc);

		verify(_view).handleException(_expectedException);
		assertThat(selectedAcc, is(originalAcc));
		verify(_view).selectExpenseAccForTx(originalAcc);
	}

	@Test
	public void when_creating_new_account_hierarchy()
	{
		_presenter.onCreateNewAccHierarchy("filename");

		verify(_view).handleException(_expectedException);
	}

}
