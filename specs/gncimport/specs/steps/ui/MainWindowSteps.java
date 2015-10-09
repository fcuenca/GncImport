package gncimport.specs.steps.ui;

import java.util.List;

import gncimport.adaptors.RbcExportParser;
import gncimport.models.TxData;
import gncimport.tests.data.TestFiles;
import gncimport.tests.endtoend.FileSystemDriver;
import gncimport.tests.endtoend.GncImportAppDriver;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.PendingException;

public class MainWindowSteps 
{
	private Robot robot;
	private GncImportAppDriver _app;
	private FileSystemDriver _fs;

	@Before
	public void beforeScenario()
	{
	    FailOnThreadViolationRepaintManager.install();
	    robot = BasicRobot.robotWithNewAwtHierarchy();
	    
		_fs = new FileSystemDriver();
		_fs.prepareTestFiles();	

		_app = new GncImportAppDriver(robot);		
	}
	
	@After
	public void afterScenario()
	{
		_app.shutdown();
		_app = null;
	    robot.cleanUp();
	}
	
	@Given("^the sample CSV file \"([^\"]*)\" has been loaded$")
	public void the_sample_csv_file_has_been_loaded(String fileName) throws Throwable 
	{
		_app.openCsvFile(TestFiles.CSV_1_TEST_FILE);
	}

	@Then("^the status bar shows a count of (\\d+) transactions$")
	public void the_status_bar_shows_a_count_of_transactions(int txCount) throws Throwable 
	{
		_app.shouldDisplayTransactionCountInStatusBar(txCount);
	}

	@Then("^the transction grid shows (\\d+) rows$")
	public void the_transction_grid_shows_rows(int txCount) throws Throwable 
	{
		_app.shouldDisplayTransactionGridWithTransactionCount(txCount);
	}
	
	@Then("^displayed transactions match those in loaded file$")
	public void displayed_transactions_match_those_in_loaded_file() throws Throwable 
	{
		RbcExportParser parser = new RbcExportParser(TestFiles.CSV_1_TEST_FILE);
		List<TxData> list = parser.getTransactions();
		
		_app.shouldDisplayTransactions(list);
	}

}
