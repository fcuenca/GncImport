package gncimport.tests.endtoend;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import gncimport.tests.data.TestFiles;
import gnclib.GncFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class FileSystemDriver
{
	public final String TMP_GNCIMPORT = "/tmp/.gncimport";
	public final String TMP_CHECKBOOK_NEW_XML = "/tmp/checkbook-new.xml";	
	
	public void prepareTestFiles() 
	{
		try 
		{
			copyFile(TestFiles.GNC_TEST_FILE, TMP_CHECKBOOK_NEW_XML);
			
			new File(TMP_GNCIMPORT).delete();
		} 
		catch (IOException e) 
		{
			fail("Couldn't copy test file");
		}
	}
	
	public void setupConfigFile(String cfgFilePath) throws IOException
	{
		copyFile(cfgFilePath, TMP_GNCIMPORT);
	}
	
	public void assertGncFileTxCountEquals(int expectedTxCount) throws IOException 
	{
		GncFile gnc = new GncFile(TMP_CHECKBOOK_NEW_XML);
		assertThat(gnc.getTransactionCount(), is(expectedTxCount));
	}
	
	public void assertConfigPropertyEquals(String propertyName, String expectedValue) throws IOException
	{
		File cfg = new File(TMP_GNCIMPORT);
		
		assertThat("expected config file doesn't exist", cfg.exists(), is(true));
		
		Properties p = new Properties();
		p.load(new FileInputStream(cfg));
		
		assertThat(p.getProperty(propertyName), is(expectedValue));
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
