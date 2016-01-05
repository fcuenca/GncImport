package gncimport.tests.unit;

import static org.junit.Assert.fail;
import gncimport.ConfigOptions;
import gncimport.InvalidConfigOption;

import java.util.Properties;

import org.junit.Test;

public class ConfigOptionsRejectsInvalidProperties
{
	@Test
	public void invalid_account_override_rules_are_rejected()
	{
		checkPropertyIsRejected("match.1.account", "missing bar and override text");
		checkPropertyIsRejected("match.1.account", "|missing description text");
		checkPropertyIsRejected("match.1.account", "   |blank description text");
		checkPropertyIsRejected("match.1.account", "blank override text|   ");
		checkPropertyIsRejected("match.1.account", "|");
	}

	@Test
	public void invalid_rewrite_rules_are_rejected()
	{
		checkPropertyIsRejected("match.1.rewrite", "missing bar and override text");
		checkPropertyIsRejected("match.1.rewrite", "|missing description text");
		checkPropertyIsRejected("match.1.rewrite", "   |blank description text");
		checkPropertyIsRejected("match.1.rewrite", "blank override text|   ");
		checkPropertyIsRejected("match.1.rewrite", "|");
	}
	
	@Test
	public void invalid_ignore_rules_are_rejected()
	{
		checkPropertyIsRejected("match.1.ignore", "");
		checkPropertyIsRejected("match.1.ignore", "   ");
	}
	
	@Test
	public void invalid_subaccount_rules_are_rejected()
	{
		checkPropertyIsRejected("monthly.1", "");
		checkPropertyIsRejected("monthly.1", "   ");
	}

	private void checkPropertyIsRejected(String key, String value)
	{
		try
		{
			Properties p = new Properties();
			p.setProperty(key, value);
			
			new ConfigOptions(p); // this should throw
			
			fail("expected exception not thrown for: " + key + "=" + value  );
		}
		catch (InvalidConfigOption e)
		{
			//Expected exception was caught
		}
	}
}
