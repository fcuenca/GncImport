package gncimport.specs.steps.hypodermic;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.ConfigOptions;
import gncimport.GncImportApp;
import gncimport.adaptors.RbcExportParser;
import gncimport.models.TxData;
import gncimport.tests.endtoend.FileSystemDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
	
	private Properties _properties;
	private String _csvFileName;
	private String _gncFileName;
	private String _defaultAccName;
	
	private List<MatchingRule> _matchingRules;
	
	private HypodermicAppDriver2 app()
	{
		if(_app == null)
		{
			ConfigOptions config = new ConfigOptions(_properties);				
			_app = new HypodermicAppDriver2(_defaultAccName, config);
		}
		return _app;
	}

	@Before
	public void beforeScenario()
	{
		_fs = new FileSystemDriver();
		_fs.prepareTestFiles();	
				
		_matchingRules = new ArrayList<MatchingRule>();

		_properties = new Properties();
		_csvFileName = "";
		_gncFileName = "";
		_defaultAccName = GncImportApp.DEFAULT_TARGET_ACCOUNT;
	}
	
	@After
	public void afterScenario()
	{
	}
	
	@Given("^transactions have been exported into \"([^\"]*)\"$")
	public void transactions_have_been_exported_into(String csvFileName) throws Throwable
	{
		_csvFileName = csvFileName;
	}

	@Given("^accounting data file \"([^\"]*)\" with default account \"([^\"]*)\"$")
	public void accounting_data_file_with_default_account(String gncFileName, String defaultAccName) throws Throwable
	{
		_gncFileName = gncFileName;
		_defaultAccName = defaultAccName;
	}
	
	@When("^the accounting file is loaded$")
	public void the_accounting_file_is_loaded() throws Throwable
	{
		//TODO: move path resolution out of the app driver (?)
		app().openGncFile(_gncFileName);
	}
	
	@When("^the transaction file is loaded$")
	public void the_transaction_file_is_loaded() throws Throwable 
	{
		app().openCsvFile(_csvFileName);
	}

	@When("^the target account hierarchy is set to \"([^\"]*)\"$")
	public void the_target_account_hierarchy_is_set_to(String accountName) throws Throwable
	{
		app().selectTargetAccHierarchy(accountName);
	}

	@Given("^the following account override rules have been defined:$")
	public void the_following_account_override_rules_have_been_defined(List<MatchingRule> matchingRules) throws Throwable 
	{
		int i = 1;
		for (MatchingRule rule : matchingRules)
		{
			_properties.setProperty("match." + i +".account", rule.desc + "|" + rule.override);
			i++;
		}		
	}

	@Then("^all other transactions are associated with \"([^\"]*)\"$")
	public void all_other_transactions_are_associated_with(String expectedAccName) throws Throwable
	{
		for (int i = 0; i < app().observedGridSize(); i++)
		{
			String txDesc = app().observedTxAtRow(i).trim();
						
			Boolean unmatched = true;
			for (MatchingRule rule : _matchingRules)
			{
				if(txDesc.matches(rule.desc))
				{
					unmatched = false;
				}				
			}
			
			if(unmatched)
			{
				String account = app().observedAccountAtRow(i).trim();			
				assertThat("mismatch detected at row: " + i, account, is(expectedAccName));		
			}
		}
	}

	@Then("^the app displays (\\d+) transactions$")
	public void the_app_displays_count_transactions(int txCount) throws Throwable 
	{
		assertThat(app().observedTxCount(), is(txCount));
	}
	
	@Then("^displayed transactions match those in loaded file$")
	public void displayed_transactions_match_those_in_loaded_file() throws Throwable 
	{
		RbcExportParser parser = new RbcExportParser(app().loadedCsvFile());
		List<TxData> list = parser.getTransactions();
		
		assertThat("Not enough transactions found", list.size(), is(app().observedGridSize()));
		
		for (int i = 0; i < app().observedGridSize(); i++)
		{
			String row = app().observedTxAtRow(i);
			String txDesc = list.get(i).description;
			
			assertThat("mismatch detected at row: ", row, is(txDesc));		
		}
	}
	

	@Then("^all transactions are associated with \"([^\"]*)\"$")
	public void all_transactions_are_associated_with(String expectedAccountName) throws Throwable 
	{
		for (int i = 0; i < app().observedGridSize(); i++)
		{
			String account = app().observedAccountAtRow(i);
			
			assertThat("mismatch detected at row: " + i, account, is(expectedAccountName));		
		}
	}
	
	@Then("^all transactions matching \"([^\"]*)\" are associated with the account \"([^\"]*)\"$")
	public void all_transactions_matching_are_associated_with_the_account(String description, String accName) throws Throwable 
	{
		_matchingRules.add(new MatchingRule(description, accName));
		
		Boolean found = false;
		
		for (int i = 0; i < app().observedGridSize(); i++)
		{
			String txDesc = app().observedTxAtRow(i).trim();
			String account = app().observedAccountAtRow(i).trim();
						
			if(txDesc.matches(description))
			{
				found = true;
				assertThat("mismatch detected at row: " + i, account, is(accName));		
			}
		}
		
		assertThat("No transaction matching: " + description + " was found.", found, is(true));
	}
}
