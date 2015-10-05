package gncimport.specs.steps.hypodermic;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import gncimport.adaptors.RbcExportParser;
import gncimport.models.TxData;
import gncimport.tests.endtoend.FileSystemDriver;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.PendingException;


public class MainWindowSteps 
{
	private HypodermicAppDriver _app;
	private FileSystemDriver _fs;
	
	@Before
	public void beforeScenario()
	{
		_fs = new FileSystemDriver();
		_fs.prepareTestFiles();			

		_app = new HypodermicAppDriver();
	}
	
	@After
	public void afterScenario()
	{
	}

	@Given("^the sample file \"([^\"]*)\" has been loaded$")
	public void the_sample_file_has_been_loaded(String fileName) throws Throwable 
	{
		_app.openCsvFile(fileName);
	}

	@Then("^the status bar shows a count of (\\d+) transactions$")
	public void the_status_bar_shows_a_count_of_transactions(int txCount) throws Throwable 
	{
		assertThat(_app.observedTxCount(), is(txCount));
	}

	@Then("^the transction grid shows (\\d+) rows$")
	public void the_transction_grid_shows_rows(int txCount) throws Throwable 
	{
		assertThat(_app.observedGridSize(), is(txCount));
	}
	
	@Then("^displayed transactions match those in loaded file$")
	public void displayed_transactions_match_those_in_loaded_file() throws Throwable 
	{
		RbcExportParser parser = new RbcExportParser(_app.loadedCvsFile());
		List<TxData> list = parser.getTransactions();
		
		assertThat("Not enough transactions found", list.size(), is(_app.observedGridSize()));
		
		for (int i = 0; i < _app.observedGridSize(); i++)
		{
			String row = _app.observedTxAtRow(i);
			String txDesc = list.get(i).description;
			
			assertThat("mismatch detected at row: ", row, is(txDesc));		
		}
	}

}
