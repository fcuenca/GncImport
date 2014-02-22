package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.boundaries.TxImportModel;
import gncimport.boundaries.TxView;
import gncimport.models.AccountData;
import gncimport.models.TxData;
import gncimport.tests.data.SampleTxData;
import gncimport.ui.MainWindowPresenter;
import gncimport.ui.TxTableModel;

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
		_presenter = new MainWindowPresenter(_model, _view);
	}

	@Test
	public void resets_table_mode_based_on_date_filter()
	{
		Date fromDate = new Date();
		Date toDate = new Date();
		List<TxData> txList = SampleTxData.txDataListWithAllAccounts();

		List<AccountData> accountList = new ArrayList<AccountData>();
		accountList.add(new AccountData("Expenses", "id"));

		when(_model.filterTxList(fromDate, toDate)).thenReturn(txList);
		when(_model.getCandidateTargetAccounts()).thenReturn(accountList);

		_presenter.onFilterTxList(fromDate, toDate);

		verify(_view).displayTxData(expectedTxList.capture(), expectedAccountList.capture());

		assertThat(expectedTxList.getValue().getTransactions(), is(txList));

		accountList.add(MainWindowPresenter.OTHER_ACC_PLACEHOLDER);
		assertThat(expectedAccountList.getValue(), is(accountList));
	}
}
