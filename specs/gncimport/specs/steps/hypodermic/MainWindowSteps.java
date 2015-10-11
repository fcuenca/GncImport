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
import java.util.Map;
import java.util.Properties;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.PendingException;


public class MainWindowSteps 
{
	private HypodermicAppDriver2 _app;
	private FileSystemDriver _fs;
	private String _defaultAccName = GncImportApp.DEFAULT_TARGET_ACCOUNT;
	
	class MatchingRule
	{
		public MatchingRule(String txDesc, String account)
		{
			this.txDescription = txDesc;
			this.account = account;
		}
		
		public String txDescription;
		public String account;
	}
	
	private List<MatchingRule> _matchingRules;
	
	@Before
	public void beforeScenario()
	{
		_fs = new FileSystemDriver();
		_fs.prepareTestFiles();	
		
		ConfigOptions config = new ConfigOptions(new Properties());				
		_app = new HypodermicAppDriver2(_defaultAccName, config);
		
		_matchingRules = new ArrayList<MatchingRule>();
	}
	
	@After
	public void afterScenario()
	{
	}
	
	@Given("^the default account is \"([^\"]*)\"$")
	public void the_default_account_is(String defaultAccName) throws Throwable 
	{
		//UGLY: this step needs to be called *before* any other step in the Scenario
		_defaultAccName = defaultAccName;
		
		ConfigOptions config = new ConfigOptions(new Properties());		
		_app = new HypodermicAppDriver2(_defaultAccName, config);
	}

	@Given("^the sample CSV file \"([^\"]*)\" has been loaded$")
	public void the_sample_CSV_file_has_been_loaded(String fileName) throws Throwable 
	{
		_app.openCsvFile(fileName);
	}
	
	@Given("^the sample GNC file \"([^\"]*)\" has been loaded$")
	public void the_sample_GNC_file_has_been_loaded(String fileName) throws Throwable 
	{
		_app.openGncFile(fileName);
	}
	
	@Given("^the target account hierarchy has been set to \"([^\"]*)\"$")
	public void the_target_account_hierarchy_has_been_set_to(String accountName) throws Throwable 
	{
		_app.selectTargetAccHierarchy(accountName);
	}

	@Then("^the app displays (\\d+) transactions$")
	public void the_app_displays_count_transactions(int txCount) throws Throwable 
	{
		assertThat(_app.observedTxCount(), is(txCount));
	}
	
	@Then("^displayed transactions match those in loaded file$")
	public void displayed_transactions_match_those_in_loaded_file() throws Throwable 
	{
		RbcExportParser parser = new RbcExportParser(_app.loadedCsvFile());
		List<TxData> list = parser.getTransactions();
		
		assertThat("Not enough transactions found", list.size(), is(_app.observedGridSize()));
		
		for (int i = 0; i < _app.observedGridSize(); i++)
		{
			String row = _app.observedTxAtRow(i);
			String txDesc = list.get(i).description;
			
			assertThat("mismatch detected at row: ", row, is(txDesc));		
		}
	}
	

	@Then("^all transactions are associated with \"([^\"]*)\"$")
	public void all_transactions_are_associated_with(String expectedAccountName) throws Throwable 
	{
		for (int i = 0; i < _app.observedGridSize(); i++)
		{
			String account = _app.observedAccountAtRow(i);
			
			assertThat("mismatch detected at row: " + i, account, is(expectedAccountName));		
		}
	}

	@Given("^transaction matching rules are defined:$")
	public void transaction_matching_rules_are_defined(List<MatchingRule> matchingRules) throws Throwable 
	{
		Properties properties = new Properties();
		int i = 1;
		for (MatchingRule rule : matchingRules)
		{
			properties.setProperty("match." + i +".account", rule.txDescription + "|" + rule.account);
			i++;
		}
		
		ConfigOptions config = new ConfigOptions(properties);				
		_app = new HypodermicAppDriver2(_defaultAccName, config);
	}
	
	@Then("^all transactions matching \"([^\"]*)\" are associated with the account \"([^\"]*)\"$")
	public void all_transactions_matching_are_associated_with_the_account(String description, String accName) throws Throwable 
	{
		_matchingRules.add(new MatchingRule(description, accName));
		
		Boolean found = false;
		
		for (int i = 0; i < _app.observedGridSize(); i++)
		{
			String txDesc = _app.observedTxAtRow(i).trim();
			String account = _app.observedAccountAtRow(i).trim();
						
			if(txDesc.matches(description))
			{
				found = true;
				assertThat("mismatch detected at row: " + i, account, is(accName));		
			}
		}
		
		assertThat("No transaction matching: " + description + " was found.", found, is(true));
	}

	@Then("^all unmatched transactions are associated with \"([^\"]*)\"$")
	public void all_unmatched_transactions_are_associated_with(String accName) throws Throwable 
	{		
		for (int i = 0; i < _app.observedGridSize(); i++)
		{
			String txDesc = _app.observedTxAtRow(i).trim();
						
			Boolean unmatched = true;
			for (MatchingRule rule : _matchingRules)
			{
				if(txDesc.matches(rule.txDescription))
				{
					unmatched = false;
				}				
			}
			
			if(unmatched)
			{
				String account = _app.observedAccountAtRow(i).trim();			
				assertThat("mismatch detected at row: " + i, account, is(accName));		
			}
		}
	}


}
