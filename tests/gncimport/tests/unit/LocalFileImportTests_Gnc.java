package gncimport.tests.unit;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gncimport.models.AccountData;
import gncimport.models.TxData;
import gncimport.models.TxMatcher;
import gncimport.tests.data.SampleTxData;
import gncimport.tests.data.TestDataConfig;
import gncimport.tests.data.TestFiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocalFileImportTests_Gnc
{
	private LocalFileTxImportModel_ForTesting _model;

	@Before
	public void setUp()
	{
		_model = new LocalFileTxImportModel_ForTesting(TestDataConfig.DEFAULT_TARGET_ACCOUNT);
		_model.openGncFile(TestFiles.GNC_TEST_FILE);
	}

	@Test
	public void assigns_default_target_account_to_new_transactions()
	{
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		List<TxData> txList = _model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		assertThat(txList.get(0).targetAccount.getName(), is("Expenses"));
		assertThat(txList.get(0).targetAccount.getId(), is(TestFiles.EXPENSES_ENERO_ID));

		assertThat(txList.get(5).targetAccount.getName(), is("Expenses"));
		assertThat(txList.get(5).targetAccount.getId(), is(TestFiles.EXPENSES_ENERO_ID));
	}

	@Test
	public void changing_target_account_changes_import_txList()
	{
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		List<TxData> txList = _model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		_model.setTargetHierarchy(new AccountData("February 2014", "irrelevant-id"));

		assertThat(txList.get(5).targetAccount.getName(), is("Expenses"));
		assertThat(txList.get(5).targetAccount.getId(), is(TestFiles.EXPENSES_FEBRERO_ID));
	}

	@Test
	public void default_target_account_can_be_overriden()
	{
		_model.setTargetHierarchy(new AccountData("February 2014", "irrelevant-id"));

		assertThat(_model.getDefaultTargetHierarchyAccount().getName(), is("February 2014"));
		assertThat(_model.getDefaultTargetHierarchyAccount().getId(), is(TestFiles.FEBRERO2014_ID));

		assertThat(_model.getDefaultTargetAccount().getName(), is("Expenses"));
		assertThat(_model.getDefaultTargetAccount().getId(), is(TestFiles.EXPENSES_FEBRERO_ID));
	}

	@Test
	public void can_match_transactions_and_assigns_specific_accounts()
	{
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		TxMatcher matcher = mock(TxMatcher.class);

		when(matcher.findAccountOverride("PAYROLL DEPOSIT - WSIB ")).thenReturn("Entertainment");

		_model.setTransactionMatchingRules(matcher);

		List<TxData> txList = _model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		assertThat(txList.get(0).targetAccount.getName(), is("Expenses"));
		assertThat(txList.get(0).targetAccount.getId(), is(TestFiles.EXPENSES_ENERO_ID));

		assertThat(txList.get(5).targetAccount.getName(), is("Entertainment"));
		assertThat(txList.get(5).targetAccount.getId(), is("243b25ad61f8f5c16335a8eae231a6dc"));
	}

	@Test
	public void can_provide_default_source_account()
	{
		_model.setSourceAccount(new AccountData("Checking Account", TestFiles.CHECKINGACC_ID));

		AccountData account = _model.getSourceAccount();

		assertThat(account.getName(), is("Checking Account"));
		assertThat(account.getId(), is(TestFiles.CHECKINGACC_ID));
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
	public void can_provide_default_target_account()
	{
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		AccountData account = _model.getDefaultTargetAccount();

		assertThat(account.getName(), is("Expenses"));
		assertThat(account.getId(), is(TestFiles.EXPENSES_ENERO_ID));
	}

	@Test
	public void can_provide_default_candidate_target_account_list()
	{
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		List<AccountData> list = _model.getCandidateTargetAccounts();

		assertThat(list.size(), is(11));
		assertThat(list.get(10).getName(), is("Expenses"));
	}

	@Test
	public void can_provide_default_target_hierarchy_parent()
	{
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		AccountData account = _model.getDefaultTargetHierarchyAccount();

		assertThat(account.getName(), is("January 2014"));
		assertThat(account.getId(), is(TestFiles.ENERO2014_ID));
	}

	@Test
	public void can_provide_list_of_accounts()
	{
		List<AccountData> accounts = _model.getAccounts();

		assertThat(accounts.size(), is(35));
		assertThat(accounts.get(0).getName(), is("Root Account"));
		assertThat(accounts.get(8).getName(), is("Monthly Expenses"));
		assertThat(accounts.get(21).getName(), is("Expenses"));
	}

	@Test
	public void can_save_to_new_file()
	{
		_model.setSourceAccount(new AccountData("Checking Account", TestFiles.CHECKINGACC_ID));
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		List<TxData> txList = SampleTxData.txDataListWithAllAccounts();

		int initialTxCount = _model.getTxCount();

		_model.saveTxTo(txList, "new-file.xml");

		assertThat(_model.detectedFileName, is("new-file.xml"));
		assertThat(_model.detectedTransactions, is(txList));
		assertThat(_model.detectedSourceAccId, is(TestFiles.CHECKINGACC_ID));

		assertThat(_model.getTxCount(), is(initialTxCount + txList.size()));
	}

	@Test
	public void some_transactions_can_be_ignored_while_saving()
	{
		_model.setSourceAccount(new AccountData("Checking Account", TestFiles.CHECKINGACC_ID));
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		List<TxData> txList = SampleTxData.txDataListWithAllAccounts();

		txList.get(1).doNotImport = true;
		txList.get(3).doNotImport = true;
		txList.get(4).doNotImport = true;

		int initialTxCount = _model.getTxCount();

		_model.saveTxTo(txList, "new-file.xml");

		assertThat(_model.getTxCount(), is(initialTxCount + txList.size() - 3));
	}

	@Test
	public void selects_equivalent_account_under_new_hierarchy_if_available()
	{
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		List<TxData> txList = _model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		txList.get(10).targetAccount = new AccountData("Supplies", "the-id");

		_model.setTargetHierarchy(new AccountData("February 2014", "irrelevant-id"));

		assertThat(txList.get(10).targetAccount.getName(), is("Supplies"));
		assertThat(txList.get(10).targetAccount.getId(), is(TestFiles.SUPPLIES_FEB_ID));
	}

	@Test
	public void preserves_target_account_selection_without_equivalent_under_new_hierarchy()
	{
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		List<TxData> txList = _model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		txList.get(10).targetAccount = new AccountData("Some Esoteric Account", "the-id");

		_model.setTargetHierarchy(new AccountData("February 2014", "irrelevant-id"));

		assertThat(txList.get(10).targetAccount.getName(), is("Some Esoteric Account"));
		assertThat(txList.get(10).targetAccount.getId(), is("the-id"));
	}

	@Test(expected = IllegalStateException.class)
	public void changing_target_hierarcy_before_opening_gnc_causes_error()
	{
		_model = new LocalFileTxImportModel_ForTesting(TestDataConfig.DEFAULT_TARGET_ACCOUNT);
		_model.setTargetHierarchy(new AccountData("February 2014", "irrelevant-id"));
	}

	@Test
	public void changing_target_account_changes_candidate_account_list()
	{
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		_model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);

		List<AccountData> initialCandidates = _model.getCandidateTargetAccounts();

		_model.setTargetHierarchy(new AccountData("February 2014", "irrelevant-id"));

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

		model.openGncFile(TestFiles.GNC_TEST_FILE);
		model.setSourceAccount(new AccountData("Checking Account", TestFiles.CHECKINGACC_ID));

		model.setTargetHierarchy(new AccountData("January 2014", "irrelevant-id"));

		assertThat(model.getDefaultTargetAccount().getName(), is("January 2014"));
		assertThat(model.getDefaultTargetAccount().getId(), is(TestFiles.ENERO2014_ID));
	}

	@Test
	public void defaults_to_hierarchy_parent_if_parent_has_no_children()
	{
		_model.setTargetHierarchy(new AccountData("Special Expenses", "irrelevant-id"));

		assertThat(_model.getDefaultTargetAccount().getName(), is("Special Expenses"));
		assertThat(_model.getDefaultTargetAccount().getId(), is(TestFiles.SPECIAL_EXPENSES_ID));
	}

	@Test
	public void selecting_hierarchy_with_no_children_gives_only_parent_as_candidate()
	{
		_model.setTargetHierarchy(new AccountData("Special Expenses", "irrelevant-id"));

		List<AccountData> list = _model.getCandidateTargetAccounts();

		assertThat(list.size(), is(1));
		assertThat(list.get(0).getName(), is("Special Expenses"));
		assertThat(list.get(0).getId(), is(TestFiles.SPECIAL_EXPENSES_ID));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void resetting_target_hierarchy_also_affects_filtered_out_transactions()
	{
		_model.setTargetHierarchy(new AccountData("January 2014", "ignored-id"));

		_model.fetchTransactionsFrom(TestFiles.CSV_1_TEST_FILE);
		_model.filterTxList(new Date(2014 - 1900, 0, 10), new Date(2014 - 1900, 0, 30));

		_model.setTargetHierarchy(new AccountData("February 2014", "irrelevant-id"));

		List<TxData> txList = _model.filterTxList(new Date(0, 0, 1), new Date(3000 - 1900, 0, 1));

		assertThat(txList.get(0).targetAccount.getId(), is(TestFiles.EXPENSES_FEBRERO_ID));
		assertThat(txList.get(10).targetAccount.getId(), is(TestFiles.EXPENSES_FEBRERO_ID));
		assertThat(txList.get(txList.size() - 1).targetAccount.getId(), is(TestFiles.EXPENSES_FEBRERO_ID));
	}

	private AccountData[] asArray(List<AccountData> list)
	{
		return list.toArray(new AccountData[list.size()]);
	}
}
