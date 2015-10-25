package gncimport.specs.steps.hypodermic;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.ConfigOptions;
import gncimport.GncImportApp;
import gncimport.adaptors.RbcExportParser;
import gncimport.models.TxData;
import gncimport.tests.data.TestFiles;
import gncimport.tests.endtoend.FileSystemDriver;
import gncimport.tests.unit.ConfigPropertyBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MainWindowSteps 
{
	private HypodermicAppDriver2 _app;
	private FileSystemDriver _fs;
	
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
		
		public AppContext()
		{
			properties = new Properties();
			csvFileName = "";
			gncFileName = "";
			defaultAccName = GncImportApp.DEFAULT_TARGET_ACCOUNT;
		}
	}
	
	private AppContext _context;
	private List<MatchingRule> _knownMatchingRules;
	
	private HypodermicAppDriver2 app()
	{
		if(_app == null)
		{
			ConfigOptions config = new ConfigOptions(_context.properties);				
			_app = new HypodermicAppDriver2(_context.defaultAccName, config);
		}
		return _app;
	}

	@Before
	public void beforeScenario()
	{
		_fs = new FileSystemDriver();
		_fs.prepareTestFiles();	
				
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
		_context.csvFileName = TestFiles.getFilePath(csvFileName);
	}

	@Given("^accounting data file \"([^\"]*)\" with default account \"([^\"]*)\"$")
	public void accounting_data_file_with_default_account(String gncFileName, String defaultAccName) throws Throwable
	{
		_context.gncFileName = TestFiles.getFilePath(gncFileName);
		_context.defaultAccName = defaultAccName;
	}
	
	@Given("^the following account override rules have been defined:$")
	public void the_following_account_override_rules_have_been_defined(List<MatchingRule> matchingRules) throws Throwable 
	{
		createPropertiesFor(matchingRules);		
	}
	
	@When("^the accounting file is loaded$")
	public void the_accounting_file_is_loaded() throws Throwable
	{
		app().openGncFile(_context.gncFileName);
	}
	
	@When("^the transaction file is loaded$")
	public void the_transaction_file_is_loaded() throws Throwable 
	{
		app().openCsvFile(_context.csvFileName);
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
		RbcExportParser parser = new RbcExportParser(_context.csvFileName);
		assertThatObservedTransactionsAreEqualTo(parser.getTransactions());
	}

	@Then("^all transactions are associated with \"([^\"]*)\"$")
	public void all_transactions_are_associated_with(String expectedAccountName) throws Throwable 
	{
		final String ALL_TRANSACTIONS = ".*";
		assertThatTransactionsAreAssociatedWithAcc(ALL_TRANSACTIONS, expectedAccountName);
	}
	
	@Then("^all transactions matching \"([^\"]*)\" are associated with the account \"([^\"]*)\"$")
	public void all_transactions_matching_are_associated_with_the_account(String expectedDesc, String expectedAccName) throws Throwable 
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
		//Builds a negative look-ahead regex: (?!one|two|three).+
		
		String regex = "(?!";
		for (MatchingRule rule : _knownMatchingRules)
		{
			regex += rule.desc + "|";
		}
		regex = regex.substring(0, regex.length()-1) + ").+";
		
		return regex;
	}

	private void assertThatTransactionsAreAssociatedWithAcc(String expectedDescRegEx, String expectedAcc) throws Exception
	{
		Boolean found = false;
		for (int i = 0; i < app().observedTxCount(); i++)
		{
			String observedDesc = app().observedTxAtRow(i).trim();
			String observedAcc = app().observedAccountAtRow(i).trim();
						
			if(observedDesc.matches(expectedDescRegEx))
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
	
	private void createPropertiesFor(List<MatchingRule> matchingRules)
	{
		ConfigPropertyBuilder builder = new ConfigPropertyBuilder();
		
		int index = 1;
		for (MatchingRule rule : matchingRules)
		{
			builder.addAccountMatchRule(index, rule.desc, rule.override);
			index++;
		}
		
		_context.properties = builder.build();
	}

}
