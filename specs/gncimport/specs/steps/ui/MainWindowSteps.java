package gncimport.specs.steps.ui;

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
	
	@Given("^the sample file \"([^\"]*)\" has been loaded$")
	public void the_sample_file_has_been_loaded(String fileName) throws Throwable 
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

}
