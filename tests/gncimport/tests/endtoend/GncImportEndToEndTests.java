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
		_appDriver.shouldDisplayTransactionCountInStatusBar(20);
		_appDriver.shouldDisplayTransactionGridWithTransactionCount(20);
	}

}
