package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.models.AccountData;
import gncimport.models.TxData;
import gncimport.models.TxImportModel;
import gncimport.tests.data.SampleTxData;
import gncimport.ui.CandidateAccList;
import gncimport.ui.MainWindowPresenter;
import gncimport.ui.TxTableModel;
import gncimport.ui.TxView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PresenterFiltersTxList
{
	@Captor
	private ArgumentCaptor<List<AccountData>> expectedAccountList;

	@Captor
	private ArgumentCaptor<TxTableModel> expectedTxList;

	private TxImportModel _model;
	private MainWindowPresenter _presenter;
	private TxView _view;

	@Before
	public void SetUp()
	{
		_model = mock(TxImportModel.class);
		_view = mock(TxView.class);
		_presenter = new MainWindowPresenter(_model, _view, null);
	}

	@Test
	public void DELETEME_resets_table_mode_based_on_date_filter()
	{
		List<TxData> txList = SampleTxData.txDataListWithAllAccounts();

		List<AccountData> accountList = new ArrayList<AccountData>();
		accountList.add(new AccountData("Expenses", "id"));

		when(_model.filterTxList(any(Date.class), any(Date.class))).thenReturn(txList);
		when(_model.getCandidateTargetAccounts()).thenReturn(accountList);

		_presenter.onFilterTxList(new Date(), new Date());

		verify(_view).displayTxData(expectedTxList.capture(), expectedAccountList.capture());
		verify(_view).displayTxCount(txList.size());

		assertThat(expectedTxList.getValue().getTransactions(), is(txList));

		accountList.add(CandidateAccList.OTHER_ACC_PLACEHOLDER);
		assertThat(expectedAccountList.getValue(), is(accountList));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void DELETEME_adjusts_upper_bound_to_be_inclusive_of_the_last_day()
	{
		Date fromDate = new Date(2012 - 1900, 10, 14, 5, 30, 27);
		Date toDate = new Date(2012 - 1900, 10, 16, 16, 33, 13);

		Date adjustedToDate = (Date) toDate.clone();
		adjustedToDate.setHours(23);
		adjustedToDate.setMinutes(59);
		adjustedToDate.setSeconds(59);

		_presenter.onFilterTxList(fromDate, toDate);

		verify(_model).filterTxList(fromDate, adjustedToDate);
	}

	@Test
	public void DELETEME_lower_bound_can_be_open()
	{
		_presenter.onFilterTxList(null, new Date());

		Date distantPast = new Date(Long.MIN_VALUE);

		verify(_model).filterTxList(eq(distantPast), any(Date.class));
	}

	@Test
	public void DELETEME_upper_bound_can_be_open()
	{
		_presenter.onFilterTxList(new Date(), null);

		Date distantFuture = new Date(Long.MAX_VALUE);

		verify(_model).filterTxList(any(Date.class), eq(distantFuture));
	}
}
