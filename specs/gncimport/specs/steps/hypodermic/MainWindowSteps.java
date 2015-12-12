package gncimport.specs.steps.hypodermic;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import gncimport.ConfigOptions;
import gncimport.GncImportApp;
import gncimport.adaptors.RbcExportParser;
import gncimport.models.MonthlyAccountParam;
import gncimport.models.TxData;
import gncimport.tests.data.TestFiles;
import gncimport.tests.unit.ConfigPropertyBuilder;
import gnclib.GncFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.gnucash.xml.gnc.Account;
import org.gnucash.xml.gnc.Transaction;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MainWindowSteps
{
	private HypodermicAppDriver3 _app;

	class MatchingRule
	{
		public MatchingRule(String txDescription, String account)
		{
			this.desc = txDescription;
			this.override = account;
		}

		public String desc;
		public String override;
	}
	
	class AppContext
	{
		public Properties properties;
		public String csvFileName;
		public String gncFileName;
		public String defaultAccName;
		public List<MonthlyAccountParam> subAccountList;

		public AppContext()
		{
			properties = new Properties();
			csvFileName = "";
			gncFileName = "";
			defaultAccName = GncImportApp.DEFAULT_TARGET_ACCOUNT;
			subAccountList = new ArrayList<MonthlyAccountParam>();
		}
		
		public String csvFullPath()
		{
			return TestFiles.getFilePath(csvFileName);
		}
		
		public String gncFullPath()
		{
			return TestFiles.getFilePath(gncFileName);
		}

		public String tmpGncFullPath()
		{
			return "/tmp/TMP_" +  gncFileName;
		}
	}

	private AppContext _context;
	private List<MatchingRule> _knownMatchingRules;

	private HypodermicAppDriver3 app()
	{
		if (_app == null)
		{
			ConfigOptions config = new ConfigOptions(_context.properties);
			_app = new HypodermicAppDriver3(_context.defaultAccName, config);
		}
		return _app;
	}

	@Before
	public void beforeScenario()
	{
		_knownMatchingRules = new ArrayList<MatchingRule>();
		_context = new AppContext();
	}

	@After
	public void afterScenario()
	{
	}

	@Given("^transactions have been exported into \"([^\"]*)\"$")
	public void transactions_have_been_exported_into(String csvFileName) throws Throwable
	{
		_context.csvFileName = csvFileName;
	}

	@Given("^accounting data file \"([^\"]*)\" with default account \"([^\"]*)\"$")
	public void accounting_data_file_with_default_account(String gncFileName, String defaultAccName) throws Throwable
	{
		_context.gncFileName = gncFileName; 
		_context.defaultAccName = defaultAccName;
	}

	@Given("^the following account override rules have been defined:$")
	public void the_following_account_override_rules_have_been_defined(List<MatchingRule> matchingRules)
			throws Throwable
	{
		_context.properties.putAll(createMatchingRules(matchingRules));
	}
	
	@Given("^the following description rewrite rules have been defined:$")
	public void the_following_description_rewrite_rules_have_been_defined(List<MatchingRule> rewriteRules) throws Throwable
	{
		_context.properties.putAll(createRewriteRules(rewriteRules));
	}

	@When("^the accounting file is loaded$")
	public void the_accounting_file_is_loaded() throws Throwable
	{
		app().openGncFile(_context.gncFullPath());
	}

	@When("^the transaction file is loaded$")
	public void the_transaction_file_is_loaded() throws Throwable
	{
		app().openCsvFile(_context.csvFullPath());
	}

	@When("^the target account hierarchy is set to \"([^\"]*)\"$")
	public void the_target_account_hierarchy_is_set_to(String accountName) throws Throwable
	{
		app().selectTargetAccHierarchy(accountName);
	}

	@Then("^the app displays (\\d+) transactions$")
	public void the_app_displays_count_transactions(int txCount) throws Throwable
	{
		assertThat(app().observedTxCount(), is(txCount));
	}

	@Then("^displayed transactions match those in loaded file$")
	public void displayed_transactions_match_those_in_loaded_file() throws Throwable
	{
		RbcExportParser parser = new RbcExportParser(_context.csvFullPath());
		assertThatObservedTransactionsAreEqualTo(parser.getTransactions());
	}
	
	class ExpectedTxFields
	{
		public Date date;
		public String description;
		public float amount;
	}
	
	@Then("^transaction data displayed includes:$")
	public void transaction_data_displayed_includes(List<ExpectedTxFields> expectedFields) throws Throwable
	{		
		for (ExpectedTxFields fields : expectedFields)
		{
			assertThat("Not found: " + fields.date + " " + fields.description + " " + fields.amount,
					observedTxListContains(fields), is(true));
		}
	}

	@Then("^all transactions are associated with \"([^\"]*)\"$")
	public void all_transactions_are_associated_with(String expectedAccountName) throws Throwable
	{
		final String ALL_TRANSACTIONS = ".*";
		assertThatTransactionsAreAssociatedWithAcc(ALL_TRANSACTIONS, expectedAccountName);
	}

	@Then("^all transactions matching \"([^\"]*)\" are associated with the account \"([^\"]*)\"$")
	public void all_transactions_matching_are_associated_with_the_account(String expectedDesc, String expectedAccName)
			throws Throwable
	{
		_knownMatchingRules.add(new MatchingRule(expectedDesc, expectedAccName));
		assertThatTransactionsAreAssociatedWithAcc(expectedDesc, expectedAccName);
	}

	@Then("^all other transactions are associated with \"([^\"]*)\"$")
	public void all_other_transactions_are_associated_with(String expectedAccName) throws Throwable
	{
		final String ALL_OTHER_TRANSACTIONS = buildRegexForNonMatchingTransactionDesc();
		assertThatTransactionsAreAssociatedWithAcc(ALL_OTHER_TRANSACTIONS, expectedAccName);
	}

	@Then("^transactions can be associated with sub-accounts:$")
	public void transactions_can_be_associated_with_sub_accounts(DataTable expectedAccounts) throws Throwable
	{
		List<List<String>> actual = buildObservedAccountList(expectedAccounts.topCells());
		expectedAccounts.diff(actual);
	}

	@Then("^all accounts offered belong to the selected target account hierarchy$")
	public void all_accounts_offered_belong_to_the_selected_target_account_hierarchy() throws Throwable
	{
		String rootId = app().observedIdForTargetHierarchyRoot();
		List<String> parentIds = app().observedParentsForTargetHierarchyAccounts();

		for (int i = 0; i < parentIds.size(); i++)
		{
			assertThat("mismatch at row: " + i, parentIds.get(i), is(rootId));
		}
	}

	@Then("^transactions can be associated with accounts outside the target hierarchy:$")
	public void transactions_can_be_associated_with_accounts_outside_the_target_hierarchy(List<String> expecteAccNames)
			throws Throwable
	{
		List<String> actualAccNames = app().observedAccountList();

		for (String accName : expecteAccNames)
		{
			assertThat("could't find: " + accName, actualAccNames.contains(accName), is(true));
		}
	}

	@Given("^the following ignore rules have been defined:$")
	public void the_following_ignore_rules_have_been_defined(List<String> rules) throws Throwable
	{
		_context.properties.putAll(createIgnoreRules(rules));
	}

	@Then("^the number of ignored transactions is (\\d+)$")
	public void the_number_of_ignored_transactions_is(int count) throws Throwable
	{
		assertThat(app().observedIgnoreCount(), is(count));
	}

	@When("^transactions are imported$")
	public void transactions_are_imported() throws Throwable
	{
		app().importTransactionsTo(_context.tmpGncFullPath());
	}

	@Then("^the \"([^\"]*)\" subaccount of \"([^\"]*)\" receives (\\d+) new transactions with \"([^\"]*)\" as source account$")
	public void the_subaccount_of_receives_new_transactions_with_as_source_account(String subAcc, String parentAcc, int txCount, String sourceAccName) throws Throwable 
	{
		int beforeCount = countTransactionsForSubAccount(_context.gncFullPath(), subAcc, parentAcc, sourceAccName);
		int afterCount = countTransactionsForSubAccount(_context.tmpGncFullPath(), subAcc, parentAcc, sourceAccName);
			
		assertThat(afterCount - beforeCount, is(txCount));
	}

	@Given("^the source account is set to \"([^\"]*)\"$")
	public void the_source_account_is_set_to(String accName) throws Throwable
	{
		app().selectSourceAccount(accName);
	}
	
	@When("^the description for transaction reading \"([^\"]*)\" is changed to \"([^\"]*)\"$")
	public void the_description_for_transaction_reading_is_changed_to(String originalDesc, String editedDesc) throws Throwable
	{
		app().editTxDescription(originalDesc + ".*", editedDesc);
	}

	@Then("^a transaction with description \"([^\"]*)\" is imported$")
	public void a_transaction_with_description_is_imported(String txDesc) throws Throwable
	{
		GncFile gnc = new GncFile(_context.tmpGncFullPath());
		
		assertThat(gnc.findTransactionsMatching(txDesc).size(), is(not(0)));
	}

	@Then("^no transaction has the description \"([^\"]*)\"$")
	public void no_transaction_has_the_description(String txDesc) throws Throwable
	{
		GncFile gnc = new GncFile(_context.tmpGncFullPath());
		
		assertThat(gnc.findTransactionsMatching(txDesc + ".*").size(), is(0));
	}
	
	@When("^account for transactions matching \"([^\"]*)\" is set to \"([^\"]*)\"$")
	public void account_for_transactions_matching_is_set_to(String txDesc, String accName) throws Throwable
	{
		app().setAccountForTransactionsMatching(accName, ".*" + txDesc + ".*");
	}
	
	@When("^transactions matching \"([^\"]*)\" are set to be ignored$")
	public void transactions_matching_are_set_to_be_ignored(String txDesc) throws Throwable
	{
		app().ignoreTransactionsMatching(txDesc + ".*");
	}
	
	@When("^a date filter from \"([^\"]*)\" to \"([^\"]*)\" is applied$")
	public void a_date_filter_from_to_is_applied(Date start, Date end) throws Throwable
	{
		app().filterTransactions(start, end);
	}

	@Then("^all transactions fall after \"([^\"]*)\" and before \"([^\"]*)\"$")
	public void all_transactions_fall_after_and_before(Date start, Date end) throws Throwable
	{
		for(TxData tx : app().observedTxData())
		{
			assertThat(tx.date, is(both(greaterThanOrEqualTo(start)).and(lessThanOrEqualTo(end))));		
		}
	}
	
	@Given("^default sub-account list:$")
	public void default_sub_account_list(List<MonthlyAccountParam> subAccounts) throws Throwable
	{
		_context.subAccountList = subAccounts;
	}

	@When("^accounts for \"([^\"]*)\" are created under \"([^\"]*)\" with the name \"([^\"]*)\"$")
	public void accounts_for_are_created_under_with_the_name(String month, List<String> parentAccName, String newAccName) throws Throwable
	{
		app().createAccounts(month, parentAccName, newAccName, _context.subAccountList, _context.tmpGncFullPath());
	}
	
	class ExpectedSubAccounts
	{
		public String code;
		public String account;
	}

	@Then("^a new account hierarchy \"([^\"]*)\" is created under \"([^\"]*)\" with code \"([^\"]*)\" and subaccounts:$")
	public void a_new_account_hierarchy_is_created_under_with_code_and_subaccounts(
			String newAccName, List<String> pathToParentAcc, String newAccCode, 
			List<ExpectedSubAccounts> subAccounts) throws Throwable
	{
		GncFile orig = new GncFile(_context.gncFullPath());
		GncFile updated = new GncFile(_context.tmpGncFullPath());
		
		assertThat(updated.getAccountCount(), is(orig.getAccountCount() + 1 + subAccounts.size()));
		assertThatGncFileContainsAccWithCodeUnderParent(updated, newAccName, newAccCode, pathToParentAcc);
		assertThatGncFileContainsAccWithSubAccs(updated, newAccName, subAccounts);
	}

	private void assertThatGncFileContainsAccWithSubAccs(GncFile updated, String newAccName,
			List<ExpectedSubAccounts> subAccounts)
	{
		for (ExpectedSubAccounts expectedSubAcc : subAccounts)
		{
			Account subAcc = findSubAccountByName(updated, expectedSubAcc.account, newAccName);
			
			assertThat(subAcc.getCode(), is(expectedSubAcc.code));
		}
	}

	private void assertThatGncFileContainsAccWithCodeUnderParent(GncFile updated, String newAccName, String newAccCode,
			List<String> pathToParentAcc)
	{
		Account newAccount = updated.findAccountByName(newAccName);
		assertThat(newAccount.getCode(), is(newAccCode));

		String parentId = newAccount.getParent().getValue();
		for (int i = pathToParentAcc.size() - 1; i >= 0; i--)
		{
			Account parentAcc = updated.findAccountById(parentId);		
			assertThat(parentAcc.getName(), is(pathToParentAcc.get(i)));
			parentId = parentAcc.getParent().getValue();
		}
	}
	
	private Properties createMatchingRules(List<MatchingRule> matchingRules)
	{
		ConfigPropertyBuilder builder = new ConfigPropertyBuilder();
		
		int index = 1;
		for (MatchingRule rule : matchingRules)
		{
			builder.addAccountMatchRule(index, rule.desc, rule.override);
			index++;
		}
		
		return builder.build();
	}
	
	private Properties createRewriteRules(List<MatchingRule> rewriteRules)
	{
		ConfigPropertyBuilder builder = new ConfigPropertyBuilder();
		
		int index = 1;
		for (MatchingRule rule : rewriteRules)
		{
			builder.addDescRewriteRule(index, rule.desc, rule.override);
			index++;
		}
		
		return builder.build();
	}

	private Properties createIgnoreRules(List<String> rules)
	{
		ConfigPropertyBuilder builder = new ConfigPropertyBuilder();
		
		int index = 1;
		for (String rule : rules)
		{
			builder.addTransactionIgnoreRule(index, rule);
			index++;
		}
		
		return builder.build();
	}

	private List<List<String>> buildObservedAccountList(List<String> tableHeaders)
	{
		List<List<String>> actual = new ArrayList<List<String>>();

		actual.add(tableHeaders);

		List<String> accounts = app().observedTagetHierarchyAccounts();

		int i = 1;
		for (String accName : accounts)
		{
			actual.add(Arrays.asList(Integer.toString(i), accName));
			i++;
		}
		return actual;
	}

	private String buildRegexForNonMatchingTransactionDesc()
	{
		// Builds a negative look-ahead regex: (?!one|two|three).+

		String regex = "(?!";
		for (MatchingRule rule : _knownMatchingRules)
		{
			regex += rule.desc + "|";
		}
		regex = regex.substring(0, regex.length() - 1) + ").+";

		return regex;
	}

	private void assertThatTransactionsAreAssociatedWithAcc(String expectedDescRegEx, String expectedAcc)
			throws Exception
	{
		Boolean found = false;
		for (int i = 0; i < app().observedTxCount(); i++)
		{
			String observedDesc = app().observedTxAtRow(i).trim();
			String observedAcc = app().observedAccountAtRow(i).trim();

			if (observedDesc.matches(expectedDescRegEx))
			{
				found = true;
				assertThat("mismatch detected at row: " + i, observedAcc, is(expectedAcc));
			}
		}

		assertThat("No transaction matching: " + expectedDescRegEx + " was found.", found, is(true));
	}

	private void assertThatObservedTransactionsAreEqualTo(List<TxData> expectedTxs) throws Exception
	{
		assertThat("Not enough transactions found", expectedTxs.size(), is(app().observedTxCount()));

		for (int i = 0; i < app().observedTxCount(); i++)
		{
			String actualDesc = app().observedTxAtRow(i);
			String expectedDesc = expectedTxs.get(i).description;

			assertThat("mismatch detected at row: ", actualDesc, is(expectedDesc));
		}
	}

	private int countTransactionsForSubAccount(final String gncFilePath, String subAcc, String parentAcc, String sourceAccName)
			throws IOException
	{
		GncFile gnc = new GncFile(gncFilePath);
		Account subAccount = findSubAccountByName(gnc, subAcc, parentAcc);
		Account sourceAcc = gnc.findAccountByName(sourceAccName);
		
		List<Transaction> txs = gnc.findTransactionsForTargetAccount(subAccount.getId().getValue());
		int count = 0;
		
		for (Transaction transaction : txs)
		{
			if(transaction.getSplits().getSplit().get(1).getAccount().getValue().equals(sourceAcc.getId().getValue()))
			{
				count++;
			}
		}
		
		return count;
	}
	
	private Account findSubAccountByName(GncFile gnc, String accName, String parentAccName)
	{		
		Account parent = gnc.findAccountByName(parentAccName);
		
		assertThat("Cannot find parent account: " + parentAccName, parent, is(notNullValue()));
		
		for (Account acc : gnc.getAccounts())
		{
			if(acc.getParent() != null && acc.getParent().getValue().equals(parent.getId().getValue()))
			{
				if(acc.getName().equals(accName))
					return acc;				
			}
		}
		
		fail("Cannot find account " + accName + " under " + parentAccName);
		
		return null;
	}
	
	private boolean observedTxListContains(ExpectedTxFields f)
	{
		boolean found = false;
		for(TxData tx : app().observedTxData())
		{
			if(tx.date.equals(f.date) && tx.description.trim().equals(f.description.trim()) && f.amount == f.amount)
			{
				found = true;
				break;
			}
		}
		return found;
	}
}
