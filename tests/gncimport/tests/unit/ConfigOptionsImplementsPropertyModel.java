package gncimport.tests.unit;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import gncimport.ConfigOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ConfigOptionsImplementsPropertyModel
{
	private ConfigOptions _options;
	
	private Properties sampleProperties()
	{
		ConfigPropertyBuilder builder = new ConfigPropertyBuilder();
		
		builder.addTransactionIgnoreRule(1, "WEB TRANSFER");
		builder.addTransactionIgnoreRule(2, "MISC PAYMENT - RBC CREDIT CARD.*");
		
		Properties p = builder.build();
		
		return p;
	}

	
	@Before
	public void Setup()
	{
		_options = new ConfigOptions(sampleProperties());	
	}

	@Test
	public void returns_ignore_rules()
	{
		List<String> rules = new ArrayList<String>();
		
		_options.copyIgnoreRules(rules);
		
		assertThat(rules, hasSize(2));
		assertThat(rules, hasItems("WEB TRANSFER", "MISC PAYMENT - RBC CREDIT CARD.*"));
	}

}
