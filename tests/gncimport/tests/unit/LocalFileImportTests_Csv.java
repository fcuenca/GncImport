package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gncimport.models.TxMatcher;
import gncimport.tests.data.TestDataConfig;
import gncimport.tests.data.TestFiles;
import gncimport.transfer.TxData;
import gncimport.utils.ProgrammerError;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocalFileImportTests_Csv
{
	private LocalFileTxImportModel_ForTesting _model;

	@Before
	public void setUp()
	{
		_model = new LocalFileTxImportModel_ForTesting(TestDataConfig.DEFAULT_TARGET_ACCOUNT);
	}

	@Test
	public void fetches_transactions_to_import_from_file()
	{
		List<TxData> txList = _model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		assertThat(txList.size(), is(20));
		assertThat(txList.get(0).description, is("MISC PAYMENT - IMH POOL I LP "));
		assertThat(txList.get(0).amount, is(new BigDecimal("-1635.00")));
	}

	@Test
	public void leaves_account_unasigned_if_csv_file_opened_before_gnc_file()
	{
		List<TxData> txList = _model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);
		
		assertThat(txList.get(5).targetAccount, is(nullValue()));
	}	
	
	@Test
	public void matching_rules_are_ignored_when_csv_opened_before_gnc()
	{
		TxMatcher matcher = mock(TxMatcher.class);
		
		when(matcher.findAccountOverride("PAYROLL DEPOSIT - WSIB ")).thenReturn("Entertainment");
		
		_model.setTransactionMatchingRules(matcher);
		
		List<TxData> txList = _model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		assertThat(txList.get(0).targetAccount, is(nullValue()));
		assertThat(txList.get(5).targetAccount, is(nullValue()));
	}
	
	@Test
	public void can_automatically_ignore_some_transactions()
	{
		TxMatcher matcher = mock(TxMatcher.class);
		
		when(matcher.isToBeIgnored("MISC PAYMENT - RBC CREDIT CARD ")).thenReturn(true);
		when(matcher.rewriteDescription(anyString())).then(returnsFirstArg());
		
		_model.setTransactionMatchingRules(matcher);
		
		List<TxData> txList = _model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		for (TxData txData : txList)
		{
			if(txData.description.equals("MISC PAYMENT - RBC CREDIT CARD "))
			{
				assertThat("unexpected ignore flag for: " + txData.description, txData.doNotImport, is(true));
			}
			else
			{
				assertThat("unexpected ignore flag for: " + txData.description, txData.doNotImport, is(false));
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void can_filter_transactions_to_import_using_inclusive_range()
	{
		_model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		Date fromDate = new Date(2014 - 1900, 0, 10);
		Date toDate = new Date(2014 - 1900, 0, 30);

		List<TxData> txList = _model.filterTxList(fromDate, toDate);

		assertThat(txList.size(), is(13));

		assertThat(txList.get(0).date, is(new Date(2014 - 1900, 0, 10)));
		assertThat(txList.get(12).date, is(new Date(2014 - 1900, 0, 30)));
	}

	@Test(expected = ProgrammerError.class)
	public void filtering_is_valid_only_after_fetching()
	{
		_model.filterTxList(new Date(), new Date());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void filter_can_be_reset_to_re_include_filtered_out_transactions()
	{
		_model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		Date fromDate = new Date(2014 - 1900, 0, 10);
		Date toDate = new Date(2014 - 1900, 0, 30);

		_model.filterTxList(fromDate, toDate);

		fromDate = new Date(0, 0, 1);
		List<TxData> txList = _model.filterTxList(fromDate, toDate);

		assertThat(txList.size(), is(19));

		assertThat(txList.get(0).date, is(new Date(2014 - 1900, 0, 2)));
		assertThat(txList.get(18).date, is(new Date(2014 - 1900, 0, 30)));
	}
	
	@Test
	public void rewrites_transaction_descriptions_according_to_matching_rules()
	{
		TxMatcher matcher = mock(TxMatcher.class);
		
		when(matcher.rewriteDescription("MONTHLY FEE - ")).thenReturn("Bank Fee");
		
		_model.setTransactionMatchingRules(matcher);
		
		List<TxData> txList = _model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		assertThat(txList.get(3).description, is("Bank Fee"));
	}


}
