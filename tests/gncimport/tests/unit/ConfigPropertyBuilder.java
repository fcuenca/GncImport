package gncimport.tests.unit;

import java.util.Properties;

// This functionality so far is needed only in tests (both unit and specs)
// but eventually will be required in production code (once the Property Editor is implemented)
// Once that happens, it will have to be properly unit tested
public class ConfigPropertyBuilder
{		
	private Properties _props = new Properties();
	
	public Properties build()
	{
		return _props;
	}
	
	public void addAccountMatchRule(int index, String txDescPattern, String overrideAccName)
	{
		_props.setProperty("match." + index + ".account", txDescPattern + "|" + overrideAccName);
	}

	public void addTransactionIgnoreRule(int index, String txDescPattern)
	{
		_props.setProperty("match." + index + ".ignore", txDescPattern);
	}
}