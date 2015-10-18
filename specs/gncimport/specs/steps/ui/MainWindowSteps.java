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

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.PendingException;

public class MainWindowSteps
{
	private Robot robot;
	private GncImportAppDriver _app;
	private FileSystemDriver _fs;
	private String _csvFileName;

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
	
	@Given("^transactions have been exported into \"([^\"]*)\"$")
	public void transactions_have_been_exported_into(String csvFileName) throws Throwable
	{
		_csvFileName = TestFiles.getFilePath(csvFileName);
	}

	@When("^the transaction file is loaded$")
	public void the_transaction_file_is_loaded() throws Throwable
	{
		_app.openCsvFile(_csvFileName);
	}

	@Then("^the app displays (\\d+) transactions$")
	public void the_app_displays_transactions(int txCount) throws Throwable
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

	@Given("^accounting data file \"([^\"]*)\" with default account \"([^\"]*)\"$")
	public void accounting_data_file_with_default_account(String arg1, String arg2) throws Throwable
	{
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}

	@When("^the accounting file is loaded$")
	public void the_accounting_file_is_loaded() throws Throwable
	{
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}

	@When("^the target account hierarchy is set to \"([^\"]*)\"$")
	public void the_target_account_hierarchy_is_set_to(String arg1) throws Throwable
	{
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}

	@Then("^all transactions are associated with \"([^\"]*)\"$")
	public void all_transactions_are_associated_with(String arg1) throws Throwable
	{
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}

	@Given("^the following account override rules have been defined:$")
	public void the_following_account_override_rules_have_been_defined(DataTable arg1) throws Throwable
	{
		// Express the Regexp above with the code you wish you had
		// For automatic conversion, change DataTable to List<YourType>
		throw new PendingException();
	}

	@Then("^all transactions matching \"([^\"]*)\" are associated with the account \"([^\"]*)\"$")
	public void all_transactions_matching_are_associated_with_the_account(String arg1, String arg2) throws Throwable
	{
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}

	@Then("^all other transactions are associated with \"([^\"]*)\"$")
	public void all_other_transactions_are_associated_with(String arg1) throws Throwable
	{
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}

}
