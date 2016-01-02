package gncimport.tests.unit;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import gncimport.tests.data.SampleTxData;
import gncimport.transfer.AccountData;
import gncimport.transfer.TxData;
import gncimport.ui.CandidateAccList;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;
import gncimport.ui.presenters.TxBrowsePresenter;
import gncimport.ui.swing.TxTableModel;

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
	public void displays_transaction_list()
	{
		List<TxData> txList = SampleTxData.txDataListWithAllAccounts();
		
		_presenter.accept(txList, new ArrayList<AccountData>());
		
		verify(_view).displayTxData(expectedTxList.capture(), anyListOf(AccountData.class));

		assertThat(expectedTxList.getValue().getTransactions(), is(txList));
	}


	@Test
	public void sets_candidate_expense_accounts()
	{
		List<AccountData> accountList = sampleAccounts();
		
		_presenter.accept(new ArrayList<TxData>(), accountList);
		
		verify(_view).displayTxData(any(TxTableModel.class), expectedAccountList.capture());

		assertThat(expectedAccountList.getValue(), hasItems(accountList.toArray(new AccountData[0])));
	}

	@Test
	public void inserts_special_place_holder_for_OTHER_accounts()
	{
		List<AccountData> accountList = sampleAccounts();
		
		_presenter.accept(new ArrayList<TxData>(), accountList);
		
		verify(_view).displayTxData(any(TxTableModel.class), expectedAccountList.capture());

		assertThat(expectedAccountList.getValue(), hasSize(accountList.size() + 1));
		assertThat(expectedAccountList.getValue(), hasItem(CandidateAccList.OTHER_ACC_PLACEHOLDER));
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

	private List<AccountData> sampleAccounts()
	{
		List<AccountData> accountList = new ArrayList<AccountData>();
		accountList.add(new AccountData("Groceries", "id-1"));
		accountList.add(new AccountData("Entertainment", "id-2"));
		accountList.add(new AccountData("Misc Expenses", "id-3"));

		return accountList;
	}


}
