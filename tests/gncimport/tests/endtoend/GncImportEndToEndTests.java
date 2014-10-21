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
	}

	@Test
	public void browse_transactions_from_csv_bank_export()
	{
		_app.openCsvFile(_fs.CSV_1_TEST_FILE);

		_app.shouldDisplayTransactionCountInStatusBar(20);
		_app.shouldDisplayTransactionGridWithTransactionCount(20);
	}

	@Test
	public void save_imported_transactions() throws IOException
	{
		_app.openGncFile(_fs.TMP_CHECKBOOK_NEW_XML);
		_app.selectSourceAccount(new String[] {"Assets", "Current Assets", "Checking Account"});
		_app.selectTargetAccount(new String[] {"Gastos Mensuales", "Year 2014", "Febrero 2014"});
		
		_app.openCsvFile(_fs.CSV_1_TEST_FILE);
		_app.importTransactions();

		_app.openCsvFile(_fs.CSV_2_TEST_FILE);
		_app.importTransactions();

		_fs.assertGncFileTxCountEquals(93);
	}
	
	@Test
	public void new_transactions_associated_by_default_to_general_expenses()
	{
		_app.openGncFile(_fs.TMP_CHECKBOOK_NEW_XML);
		_app.selectSourceAccount(new String[] {"Assets", "Current Assets", "Checking Account"});
		_app.selectTargetAccount(new String[] {"Gastos Mensuales", "Year 2014", "Febrero 2014"});
		
		_app.openCsvFile(_fs.CSV_1_TEST_FILE);
		_app.shouldAssociateAllTransactionsTo("Gastos Varios");
		
	}

	@Test
	public void matches_know_transaction_patterns_with_other_accounts()
	{
		_fs.setupConfigFile(_fs.CFG_WITH_MATCHING_RULES);
		
		_app.openGncFile(_fs.TMP_CHECKBOOK_NEW_XML);
		_app.selectSourceAccount(new String[] {"Assets", "Current Assets", "Checking Account"});
		_app.selectTargetAccount(new String[] {"Gastos Mensuales", "Year 2014", "Febrero 2014"});
		
		_app.openCsvFile(_fs.CSV_1_TEST_FILE);
		
		_app.shouldAssociateTransactionsToAccount("MISC PAYMENT - IMH POOL I LP", "Departamento");
		_app.shouldAssociateTransactionsToAccount("MISC PAYMENT - GOODLIFE CLUBS", "Salud");
		
	}
}
