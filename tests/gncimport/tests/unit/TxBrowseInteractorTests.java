package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.models.TxImportModel;
import gncimport.tests.data.SampleTxData;
import gncimport.transfer.AccountData;
import gncimport.transfer.TxData;

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
public class TxBrowseInteractorTests
{
	@Captor
	private ArgumentCaptor<List<AccountData>> expectedAccountList;

	@Captor
	private ArgumentCaptor<List<TxData>> expectedTxList;

	private TxBrowseInteractor.OutPort _outPort;
	private TxImportModel _model;
	private TxBrowseInteractor _interactor;
	
	@Before
	public void Setup()
	{
		_outPort = mock(TxBrowseInteractor.OutPort.class);
		_model = mock(TxImportModel.class);
		
		_interactor = new TxBrowseInteractor(_outPort, _model);
	}

	@Test
	public void fetches_transactions_into_the_model()
	{
		_interactor.fetchTransactions("fileName");
		
		verify(_model).fetchTransactionsFrom("fileName");
	}
	
	@Test
	public void updates_view_with_transaction_list()
	{
		List<TxData> txList = SampleTxData.txDataListWithAllAccounts();

		List<AccountData> accountList = new ArrayList<AccountData>();
		accountList.add(new AccountData("Expenses", "id"));

		when(_model.fetchTransactionsFrom(anyString())).thenReturn(txList);
		when(_model.getCandidateTargetAccounts()).thenReturn(accountList);

		_interactor.fetchTransactions("/path/to/input/file.csv");

		verify(_outPort).accept(expectedTxList.capture(), expectedAccountList.capture());
		assertThat(expectedTxList.getValue(), is(txList));
		assertThat(expectedAccountList.getValue(), is(accountList));
		
		verify(_outPort).fileWasOpened("/path/to/input/file.csv");
	}

	@Test
	public void filters_the_model_with_supplied_date_range()
	{
		Date fromDate = new Date();
		Date toDate = new Date();
		
		_interactor.filterTxList(fromDate, toDate);
		
		verify(_model).filterTxList(fromDate, toDate);
	}
	
	@Test
	public void resets_view_with_filtered_transaction_list()
	{
		List<TxData> txList = SampleTxData.txDataListWithAllAccounts();

		List<AccountData> accountList = new ArrayList<AccountData>();
		accountList.add(new AccountData("Expenses", "id"));

		when(_model.filterTxList(any(Date.class), any(Date.class))).thenReturn(txList);
		when(_model.getCandidateTargetAccounts()).thenReturn(accountList);

		_interactor.filterTxList(new Date(), new Date());

		verify(_outPort).accept(expectedTxList.capture(), expectedAccountList.capture());
		assertThat(expectedTxList.getValue(), is(txList));
		assertThat(expectedAccountList.getValue(), is(accountList));
	}
}
