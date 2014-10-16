package gncimport.tests.endtoend;

import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.junit.Test;

public class GncImportEndToEndTests extends FestSwingJUnitTestCase
{

	private GncImportAppDriver _appDriver;

	@Override
	protected void onSetUp()
	{
		_appDriver = new GncImportAppDriver(robot());
	}

	@Test
	public void browse_transactions_on_app_init()
	{
		_appDriver.openTestInputFiles();
		_appDriver.shouldDisplayTransactionCountInStatusBar(20);
		_appDriver.shouldDisplayTransactionGridWithTransactionCount(20);
	}

	@Test
	public void save_imported_transactions()
	{
		_appDriver.openTestInputFiles();
		_appDriver.shouldSaveTransactionsToGncFile();
	}

}
