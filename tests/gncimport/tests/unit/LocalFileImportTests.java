package gncimport.tests.unit;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gncimport.models.AccountData;
import gncimport.models.LocalFileTxImportModel;
import gncimport.models.TxData;
import gncimport.models.TxMatcher;
import gncimport.tests.data.SampleTxData;
import gncimport.tests.data.TestDataConfig;
import gncimport.utils.ProgrammerError;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LocalFileImportTests
{
	private static final String SPECIAL_EXPENSES_ID = "1eb826d327e81be51e663430f5b7adb9";
	private static final String SUPPLIES_FEB_ID = "2c6be57ad1474692f6f569384668c4ac";
	private static final String FEBRERO2014_ID = "882f951395a92f8ea103fe0e9dbfbda5";
	private static final String ENERO2014_ID = "454018fbf408f8c3e607bd51f22c5373";
	private static final String CHECKINGACC_ID = "64833494284bad5fb390e84d38c65a54";
	private static final String EXPENSES_FEBRERO_ID = "1edf8498fcda9b8a677160b0b6357287";
	private static final String EXPENSES_ENERO_ID = "e31486ad3b2c6cdedccf135d13538b29";

	private class LocalFileTxImportModel_ForTesting extends LocalFileTxImportModel
	{

		public LocalFileTxImportModel_ForTesting(String defaultTargetAccName)
		{
			super(defaultTargetAccName);
		}

		public String detectedFileName;
		public String detectedSourceAccId;
		public List<TxData> detectedTransactions;

		@Override
		protected void saveToGncFile(String fileName) throws IOException
		{
			detectedFileName = fileName;
		}

		@Override
		protected void addNewTransactions(List<TxData> transactions, String sourceAccId)
		{
			detectedTransactions = transactions;
			detectedSourceAccId = sourceAccId;
			super.addNewTransactions(transactions, sourceAccId);
		}

		public int getTxCount()
		{
			return _gnc.getTransactionCount();
		}
	}

	private LocalFileTxImportModel_ForTesting _model;

	@Before
	public void setUp()
	{
		_model = new LocalFileTxImportModel_ForTesting(TestDataConfig.DEFAULT_TARGET_ACCOUNT);

		//TODO: fix temporal coupling (these are the preconditions to loading the CSV file -- specifically, setting the target hierarchy)
		_model.openGncFile(getClass().getResource("../data/checkbook.xml").getPath());
		_model.setSourceAccount(new AccountData("Checking Account", CHECKINGACC_ID));
		_model.setTargetHierarchy(new AccountData("Enero 2014", "ignored-id"));
	}

	@Test
	public void fetches_transactions_to_import_from_file()
	{
		List<TxData> txList = _model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());

		assertThat(txList.size(), is(20));
		assertThat(txList.get(0).description, is("MISC PAYMENT - IMH POOL I LP "));
		assertThat(txList.get(0).amount, is(new BigDecimal("-1635.00")));
	}

	@Test
	public void assigns_default_target_account_to_new_transactions()
	{
		List<TxData> txList = _model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());

		assertThat(txList.get(0).targetAccount.getName(), is("Expenses"));
		assertThat(txList.get(0).targetAccount.getId(), is(EXPENSES_ENERO_ID));
		
		assertThat(txList.get(5).targetAccount.getName(), is("Expenses"));
		assertThat(txList.get(5).targetAccount.getId(), is(EXPENSES_ENERO_ID));
	}
	
	//TODO: this test doesn't use the common fixture. Move to separate suite when addressing the rest of the temporal coupling issue.
	@Test
	public void leaves_account_unasigned_if_csv_file_opened_before_gnc_file()
	{
		_model = new LocalFileTxImportModel_ForTesting(TestDataConfig.DEFAULT_TARGET_ACCOUNT);

		List<TxData> txList = _model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());
		
		assertThat(txList.get(5).targetAccount, is(nullValue()));
	}			

	@Test
	public void can_match_transactions_and_assigns_specific_accounts()
	{
		TxMatcher matcher = mock(TxMatcher.class);
		
		when(matcher.findAccountOverride("PAYROLL DEPOSIT - WSIB ")).thenReturn("Entertainment");
		
		_model.setTransactionMatchingRules(matcher);
		
		List<TxData> txList = _model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());

		assertThat(txList.get(0).targetAccount.getName(), is("Expenses"));
		assertThat(txList.get(0).targetAccount.getId(), is(EXPENSES_ENERO_ID));

		assertThat(txList.get(5).targetAccount.getName(), is("Entertainment"));
		assertThat(txList.get(5).targetAccount.getId(), is("243b25ad61f8f5c16335a8eae231a6dc"));
	}

	@Test
	public void can_provide_default_source_account()
	{
		AccountData account = _model.getSourceAccount();

		assertThat(account.getName(), is("Checking Account"));
		assertThat(account.getId(), is(CHECKINGACC_ID));
	}

	@Test
	public void can_provide_default_target_account()
	{
		AccountData account = _model.getDefaultTargetAccount();

		assertThat(account.getName(), is("Expenses"));
		assertThat(account.getId(), is(EXPENSES_ENERO_ID));
	}

	@Test
	public void can_provide_default_candidate_target_account_list()
	{
		List<AccountData> list = _model.getCandidateTargetAccounts();

		assertThat(list.size(), is(11));
		assertThat(list.get(10).getName(), is("Expenses"));
	}

	@Test
	public void can_provide_default_target_hierarchy_parent()
	{
		AccountData account = _model.getDefaultTargetHierarchyAccount();

		assertThat(account.getName(), is("Enero 2014"));
		assertThat(account.getId(), is(ENERO2014_ID));
	}

	@Test
	public void can_provide_list_of_accounts()
	{
		List<AccountData> accounts = _model.getAccounts();

		assertThat(accounts.size(), is(34));
		assertThat(accounts.get(0).getName(), is("Root Account"));
		assertThat(accounts.get(7).getName(), is("Gastos Mensuales"));
		assertThat(accounts.get(20).getName(), is("Expenses"));
	}

	@Test
	public void can_save_to_new_file()
	{
		List<TxData> txList = SampleTxData.txDataListWithAllAccounts();

		int initialTxCount = _model.getTxCount();

		_model.saveTxTo(txList, "new-file.xml");

		assertThat(_model.detectedFileName, is("new-file.xml"));
		assertThat(_model.detectedTransactions, is(txList));
		assertThat(_model.detectedSourceAccId, is(CHECKINGACC_ID));

		assertThat(_model.getTxCount(), is(initialTxCount + txList.size()));
	}

	@Test
	public void some_transactions_can_be_ignored_while_saving()
	{
		List<TxData> txList = SampleTxData.txDataListWithAllAccounts();

		txList.get(1).doNotImport = true;
		txList.get(3).doNotImport = true;
		txList.get(4).doNotImport = true;

		int initialTxCount = _model.getTxCount();

		_model.saveTxTo(txList, "new-file.xml");

		assertThat(_model.getTxCount(), is(initialTxCount + txList.size() - 3));
	}

	@Test
	public void default_source_account_can_be_overriden()
	{
		List<TxData> txList = new ArrayList<TxData>();
		AccountData newAccount = new AccountData("new Account", "new-id");

		_model.setSourceAccount(newAccount);
		_model.saveTxTo(txList, "new-file.xml");

		assertThat(_model.detectedSourceAccId, is("new-id"));
	}

	@Test
	public void default_target_account_can_be_overriden()
	{
		AccountData newAccount = new AccountData("Febrero 2014", "irrelevant-id");

		_model.setTargetHierarchy(newAccount);

		assertThat(_model.getDefaultTargetHierarchyAccount().getName(), is("Febrero 2014"));
		assertThat(_model.getDefaultTargetHierarchyAccount().getId(), is(FEBRERO2014_ID));

		assertThat(_model.getDefaultTargetAccount().getName(), is("Expenses"));
		assertThat(_model.getDefaultTargetAccount().getId(), is(EXPENSES_FEBRERO_ID));
	}

	@Test
	public void changing_target_account_changes_import_txList()
	{
		List<TxData> txList = _model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());

		_model.setTargetHierarchy(new AccountData("Febrero 2014", "irrelevant-id"));

		assertThat(txList.get(5).targetAccount.getName(), is("Expenses"));
		assertThat(txList.get(5).targetAccount.getId(), is(EXPENSES_FEBRERO_ID));
	}

	@Test
	public void selects_equivalent_account_under_new_hierarchy_if_available()
	{
		List<TxData> txList = _model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());

		txList.get(10).targetAccount = new AccountData("Supplies", "the-id");

		_model.setTargetHierarchy(new AccountData("Febrero 2014", "irrelevant-id"));

		assertThat(txList.get(10).targetAccount.getName(), is("Supplies"));
		assertThat(txList.get(10).targetAccount.getId(), is(SUPPLIES_FEB_ID));
	}

	@Test
	public void preserves_target_account_selection_without_equivalent_under_new_hierarchy()
	{
		List<TxData> txList = _model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());

		txList.get(10).targetAccount = new AccountData("Some Esoteric Account", "the-id");

		_model.setTargetHierarchy(new AccountData("Febrero 2014", "irrelevant-id"));

		assertThat(txList.get(10).targetAccount.getName(), is("Some Esoteric Account"));
		assertThat(txList.get(10).targetAccount.getId(), is("the-id"));
	}

	@Test
	public void changing_target_account_changes_candidate_account_list()
	{
		_model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());

		List<AccountData> initialCandidates = _model.getCandidateTargetAccounts();

		_model.setTargetHierarchy(new AccountData("Febrero 2014", "irrelevant-id"));

		List<AccountData> newCandidates = _model.getCandidateTargetAccounts();

		assertThat(newCandidates, not(hasItems(asArray(initialCandidates))));
		assertThat(newCandidates.size(), is(11));
	}

	@Test(expected = IllegalArgumentException.class)
	public void target_hierarchy_must_exist()
	{
		AccountData newAccount = new AccountData("Doesn't Exist", "irrelevant-id");

		_model.setTargetHierarchy(newAccount);
	}

	@Test
	public void defaults_to_hierarchy_parent_if_default_acc_name_not_found()
	{
		LocalFileTxImportModel_ForTesting model = new LocalFileTxImportModel_ForTesting("doesn't exist");

		model.openGncFile(getClass().getResource("../data/checkbook.xml").getPath());
		model.setSourceAccount(new AccountData("Checking Account", CHECKINGACC_ID));

		model.setTargetHierarchy(new AccountData("Enero 2014", "irrelevant-id"));

		assertThat(model.getDefaultTargetAccount(), is(new AccountData("Enero 2014", ENERO2014_ID)));
	}

	@Test
	public void defaults_to_hierarchy_parent_if_parent_has_no_children()
	{
		_model.setTargetHierarchy(new AccountData("Special Expenses", "irrelevant-id"));

		assertThat(_model.getDefaultTargetAccount(), is(new AccountData("Special Expenses", SPECIAL_EXPENSES_ID)));
	}

	@Test
	public void selecting_hierarchy_with_no_children_gives_only_parent_as_candidate()
	{
		_model.setTargetHierarchy(new AccountData("Special Expenses", "irrelevant-id"));

		List<AccountData> list = _model.getCandidateTargetAccounts();

		assertThat(list.size(), is(1));
		assertThat(list.get(0), is(new AccountData("Special Expenses", SPECIAL_EXPENSES_ID)));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void can_filter_transactions_to_import_using_inclusive_range()
	{
		_model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());

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
		_model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());

		Date fromDate = new Date(2014 - 1900, 0, 10);
		Date toDate = new Date(2014 - 1900, 0, 30);

		_model.filterTxList(fromDate, toDate);

		fromDate = new Date(0, 0, 1);
		List<TxData> txList = _model.filterTxList(fromDate, toDate);

		assertThat(txList.size(), is(19));

		assertThat(txList.get(0).date, is(new Date(2014 - 1900, 0, 2)));
		assertThat(txList.get(18).date, is(new Date(2014 - 1900, 0, 30)));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void resetting_target_hierarchy_also_affects_filtered_out_transactions()
	{
		_model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());
		_model.filterTxList(new Date(2014 - 1900, 0, 10), new Date(2014 - 1900, 0, 30));

		_model.setTargetHierarchy(new AccountData("Febrero 2014", "irrelevant-id"));

		List<TxData> txList = _model.filterTxList(new Date(0, 0, 1), new Date(3000 - 1900, 0, 1));

		assertThat(txList.get(0).targetAccount.getId(), is(EXPENSES_FEBRERO_ID));
		assertThat(txList.get(10).targetAccount.getId(), is(EXPENSES_FEBRERO_ID));
		assertThat(txList.get(txList.size() - 1).targetAccount.getId(), is(EXPENSES_FEBRERO_ID));
	}

	private AccountData[] asArray(List<AccountData> accountList)
	{
		return accountList.toArray(new AccountData[accountList.size()]);
	}
}
