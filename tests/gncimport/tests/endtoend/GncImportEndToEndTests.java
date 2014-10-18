package gncimport.tests.endtoend;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnclib.GncFile;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.junit.Test;

public class GncImportEndToEndTests extends FestSwingJUnitTestCase
{
	private static final String TMP_CHECKBOOK_NEW_XML = "/tmp/checkbook-new.xml";
	private String _gncFilePath = getClass().getResource("../data/checkbook.xml").getPath();
	private String _csvFilePath = getClass().getResource("../data/rbc.csv").getPath();
	
	private GncImportAppDriver _app;

	@Override
	protected void onSetUp()
	{		
		prepareTestFiles();
		
		_app = new GncImportAppDriver(robot(), TMP_CHECKBOOK_NEW_XML);		
		_app.openTestInputFiles(TMP_CHECKBOOK_NEW_XML, _csvFilePath);
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
		
		assertGncFileTxCountEquals(22);
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

	
	//TODO: extract class with file-related methods
	private void prepareTestFiles() 
	{
		try 
		{
			copyFile(_gncFilePath, TMP_CHECKBOOK_NEW_XML);
		} 
		catch (IOException e) 
		{
			fail("Couldn't copy test file");
		}
	}
	
	private void assertGncFileTxCountEquals(int expectedTxCount) throws IOException 
	{
		GncFile gnc = new GncFile(TMP_CHECKBOOK_NEW_XML);
		assertThat(gnc.getTransactionCount(), is(expectedTxCount));
	}

	private static void copyFile(String source, String dest) throws IOException
	{
		InputStream is = null;
		OutputStream os = null;
		try
		{
			is = new FileInputStream(new File(source));
			os = new FileOutputStream(new File(dest));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0)
			{
				os.write(buffer, 0, length);
			}
		}
		finally
		{
			is.close();
			os.close();
		}
	}
}
