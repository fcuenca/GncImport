package gncimport.tests.unit;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.models.AccountData;
import gncimport.models.LocalFileTxImportModel;
import gncimport.models.TxData;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.gnucash.xml.gnc.Account;
import org.junit.Before;
import org.junit.Test;

public class LocalFileImportTests
{
	private static final String SUPPLIES_FEB_ID = "2c6be57ad1474692f6f569384668c4ac";
	private static final String FEBRERO2014_ID = "882f951395a92f8ea103fe0e9dbfbda5";
	private static final String ENERO2014_ID = "454018fbf408f8c3e607bd51f22c5373";
	private static final String CHECKINGACC_ID = "64833494284bad5fb390e84d38c65a54";
	private static final String EXPENSES_FEBRERO_ID = "1edf8498fcda9b8a677160b0b6357287";
	private static final String EXPENSES_ENERO_ID = "e31486ad3b2c6cdedccf135d13538b29";

	private class LocalFileTxImportModel_ForTesting extends LocalFileTxImportModel
	{

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
		_model = new LocalFileTxImportModel_ForTesting();

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
	public void assigns_target_account_to_new_transactions()
	{
		List<TxData> txList = _model.fetchTransactionsFrom(getClass().getResource("../data/rbc.csv").getPath());

		assertThat(txList.get(5).targetAccount.getName(), is("Expenses"));
		assertThat(txList.get(5).targetAccount.getId(), is(EXPENSES_ENERO_ID));
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
		List<Account> accounts = _model.getAccounts();

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

	private AccountData[] asArray(List<AccountData> accountList)
	{
		return accountList.toArray(new AccountData[accountList.size()]);
	}

}
