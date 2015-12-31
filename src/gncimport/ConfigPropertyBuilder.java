package gncimport;

import java.util.Properties;

//TODO: unit test in isolation (?)
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

	public void addDescRewriteRule(int index, String txDescPattern, String override)
	{
		_props.setProperty("match." + index + ".rewrite", txDescPattern + "|" + override);
	}

	public void addSubAccountRule(int index, String accName)
	{
		_props.setProperty("monthly." + index, accName);		
	}

}