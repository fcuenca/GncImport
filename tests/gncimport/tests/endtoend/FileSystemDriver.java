package gncimport.tests.endtoend;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import gnclib.GncFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileSystemDriver
{
	public final String TMP_CHECKBOOK_NEW_XML = "/tmp/checkbook-new.xml";	
	public final String CSV_1_TEST_FILE = getClass().getResource("../data/rbc.csv").getPath();
	public final String CSV_2_TEST_FILE = getClass().getResource("../data/rbc-visa.csv").getPath();
	
	private final String _gncFilePath = getClass().getResource("../data/checkbook.xml").getPath();
	
	public void prepareTestFiles() 
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
	
	public void assertGncFileTxCountEquals(int expectedTxCount) throws IOException 
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
