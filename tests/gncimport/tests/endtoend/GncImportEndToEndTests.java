package gncimport.tests.endtoend;

import gncimport.tests.data.TestFiles;

import java.io.File;
import java.io.IOException;

import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.junit.Test;

public class GncImportEndToEndTests extends FestSwingJUnitTestCase
{
	private GncImportAppDriver _app;
	private FileSystemDriver _fs;

	@Override
	protected void onSetUp()
	{		
		_fs = new FileSystemDriver();
		_fs.prepareTestFiles();	
	}
	
	@Test
	public void browse_transactions_from_csv_bank_export()
	{
		_app = new GncImportAppDriver(robot());		

		_app.openCsvFile(TestFiles.CSV_1_TEST_FILE);

		_app.shouldDisplayTransactionCountInStatusBar(20);
		_app.shouldDisplayTransactionGridWithTransactionCount(20);
	}

	@Test
	public void save_imported_transactions() throws IOException
	{	
		_app = new GncImportAppDriver(robot());		

		_app.openGncFile(_fs.TMP_CHECKBOOK_NEW_XML);
		_app.selectSourceAccount(new String[] {"Assets", "Current Assets", "Checking Account"});
		_app.selectTargetAccount(new String[] {"Gastos Mensuales", "Year 2014", "Febrero 2014"});
		
		_app.openCsvFile(TestFiles.CSV_1_TEST_FILE);
		_app.importTransactions();

		_app.openCsvFile(TestFiles.CSV_2_TEST_FILE);
		_app.importTransactions();

		_fs.assertGncFileTxCountEquals(93);
	}
	
	@Test
	public void by_default_everything_is_imported_into_general_expenses()
	{
		_app = new GncImportAppDriver(robot());		

		_app.openGncFile(_fs.TMP_CHECKBOOK_NEW_XML);
		_app.selectSourceAccount(new String[] {"Assets", "Current Assets", "Checking Account"});
		_app.selectTargetAccount(new String[] {"Gastos Mensuales", "Year 2014", "Febrero 2014"});
		
		_app.openCsvFile(TestFiles.CSV_1_TEST_FILE);
		
		_app.shouldAssociateAllTransactionsTo("Gastos Varios");
		_app.shouldNotIgnoreAnyTransaction();
	}
	
	@Test
	public void matches_know_transaction_patterns_with_other_accounts() throws IOException
	{
		_fs.setupConfigFile(TestFiles.CFG_WITH_MATCHING_RULES);
		
		_app = new GncImportAppDriver(robot());		

		_app.openGncFile(_fs.TMP_CHECKBOOK_NEW_XML);
		_app.selectSourceAccount(new String[] {"Assets", "Current Assets", "Checking Account"});
		_app.selectTargetAccount(new String[] {"Gastos Mensuales", "Year 2014", "Febrero 2014"});
		
		_app.openCsvFile(TestFiles.CSV_1_TEST_FILE);
		
		_app.shouldAssociateTransactionsToAccount("MISC PAYMENT - IMH POOL I LP", "Departamento");
		_app.shouldAssociateTransactionsToAccount("MISC PAYMENT - GOODLIFE CLUBS", "Salud");
	}
	
	@Test
	public void known_transactions_can_be_automatically_ignored() throws IOException
	{
		_fs.setupConfigFile(TestFiles.CFG_WITH_MATCHING_RULES);
		
		_app = new GncImportAppDriver(robot());		

		_app.openGncFile(_fs.TMP_CHECKBOOK_NEW_XML);
		_app.selectSourceAccount(new String[] {"Assets", "Current Assets", "Checking Account"});
		_app.selectTargetAccount(new String[] {"Gastos Mensuales", "Year 2014", "Febrero 2014"});
		
		_app.openCsvFile(TestFiles.CSV_1_TEST_FILE);
		
		_app.shouldIgnoreTransactionsLike("MISC PAYMENT - RBC CREDIT CARD.*");
	}
	
	@Test
	public void by_default_looks_for_files_in_user_home_directory()
	{
		_app = new GncImportAppDriver(robot());	
		_app.shouldLookForFilesInFolder(System.getProperty("user.home"));
	}

	@Test
	public void uses_last_known_location_to_open_files() throws IOException
	{
		_fs.setupConfigFile(TestFiles.CFG_WITH_LAST_LOCATIONS);

		_app = new GncImportAppDriver(robot());	
		_app.shouldLookForFilesInFolder("/tmp");
	}

	@Test
	public void falls_back_to_homeDir_if_last_known_locations_are_invalid() throws IOException
	{
		_fs.setupConfigFile(TestFiles.CFG_WITH_INVALID_LOCATIONS);

		_app = new GncImportAppDriver(robot());	
		_app.shouldLookForFilesInFolder(System.getProperty("user.home"));
	}

	@Test
	public void remembers_where_files_were_opened_the_last_time() throws IOException
	{
		_app = new GncImportAppDriver(robot());	

		_app.openGncFile(_fs.TMP_CHECKBOOK_NEW_XML);
		_app.openCsvFile(TestFiles.CSV_1_TEST_FILE);
		
		forceAppShutdown();

		_fs.assertConfigPropertyEquals("last.gnc", new File(_fs.TMP_CHECKBOOK_NEW_XML).getParent());
		_fs.assertConfigPropertyEquals("last.csv", new File(TestFiles.CSV_1_TEST_FILE).getParent());

	}
	
	private void forceAppShutdown()
	{
		_app.shutdown();
		_app = null;
		robot().cleanUp();
	}
	
	@Test
	public void creates_standardized_account_hierarchies() throws IOException
	{
		_fs.setupConfigFile(TestFiles.CFG_WITH_MONTHLY_ACCOUNTS);

		_app = new GncImportAppDriver(robot());	

		_app.openGncFile(_fs.TMP_CHECKBOOK_NEW_XML);
		_app.createAccountHierarchy(new String[] {"Gastos Mensuales", "Year 2014"}, "Marzo 2014");
		_app.selectTargetAccount(new String[] {"Gastos Mensuales", "Year 2014", "Marzo 2014", "Gastos Varios"});
	}
}
