package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.models.AccountData;
import gncimport.models.TxData;
import gncimport.tests.data.SampleTxData;
import gncimport.ui.CandidateAccList;
import gncimport.ui.TxBrowsePresenter;
import gncimport.ui.TxTableModel;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TxBrowsePresenterTests
{
	@Captor
	private ArgumentCaptor<List<AccountData>> expectedAccountList;

	@Captor
	private ArgumentCaptor<TxTableModel> expectedTxList;

	private TxView _view;
	private UIConfig _config;
	private TxBrowsePresenter _presenter;
	
	@Before
	public void Setup()
	{
		_view = mock(TxView.class);
		_config = mock(UIConfig.class);
		
		_presenter = new TxBrowsePresenter(_view, _config);
	}

	@Test
	public void adds_OTHER_placeholder_when_displaying_expense_account_list()
	{
		List<TxData> txList = SampleTxData.txDataListWithAllAccounts();
		
		List<AccountData> accountList = new ArrayList<AccountData>();
		accountList.add(new AccountData("Expenses", "id"));
		
		_presenter.accept(txList, accountList);
		
		verify(_view).displayTxData(expectedTxList.capture(), expectedAccountList.capture());

		assertThat(expectedTxList.getValue().getTransactions(), is(txList));

		accountList.add(CandidateAccList.OTHER_ACC_PLACEHOLDER);
		assertThat(expectedAccountList.getValue(), is(accountList));
	}

	@Test
	public void updates_transaction_count_in_view()
	{
		List<TxData> txList = SampleTxData.txDataListWithAllAccounts();
		
		_presenter.accept(txList, new ArrayList<AccountData>());
		
		verify(_view).displayTxCount(txList.size());
	}
	
	@Test
	public void updates_view_when_transaction_file_is_open()
	{
		_presenter.fileWasOpened("someFileName");
		
		verify(_view).updateCsvFileLabel("someFileName");
	}

	@Test
	public void remembers_location_of_last_transaction_file_opened()
	{
		_presenter.fileWasOpened("/path/to/input/file.csv");
		
		verify(_config).setLastCsvDirectory("/path/to/input");
	}



}
