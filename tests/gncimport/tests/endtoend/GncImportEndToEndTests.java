package gncimport.tests.endtoend;

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
		_app = new GncImportAppDriver(robot());		
		
		_fs.prepareTestFiles();	
		
		_app.openGncFile(_fs.TMP_CHECKBOOK_NEW_XML);
		_app.selectSourceAccount(new String[] {"Assets", "Current Assets", "Checking Account"});
		_app.selectTargetAccount(new String[] {"Gastos Mensuales", "Year 2014", "Febrero 2014"});
		_app.openCsvFile(_fs.CSV_TEST_FILE);
	}

	@Test
	public void browse_transactions_on_app_init()
	{
		_app.shouldDisplayTransactionCountInStatusBar(20);
		_app.shouldDisplayTransactionGridWithTransactionCount(20);
	}

	@Test
	public void save_imported_transactions() throws IOException
	{
		_app.importTransactions();
		_fs.assertGncFileTxCountEquals(22);
	}

	
//	@Test
//	public void importing_two_files_on_a_row()
//	{
//		_appDriver.openTestGncFile();
//		
//		_appDriver.openTestCsvFile("f1.csv");
//		_appDriver.importAllTransactions();
//		
//		_appDriver.openTestCsvFile("f2.csv");
//		_appDriver.importAllTransactions();
//
//		assertTxCountInFileEquals(30);
//	}

	
}
