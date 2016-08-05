package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import gncimport.interactors.PropertyEditInteractor;
import gncimport.interactors.PropertyEditInteractor.OutPort;
import gncimport.models.RuleModel;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@RunWith(Parameterized.class)
public class RuleTesterContractTests
{
	private RuleTester _tester;
	private RuleModel _model;

	@After
	public void tearDown() throws Exception
	{
		reset(_model);
	}

	@SuppressWarnings("rawtypes")
	@Parameterized.Parameters
	public static Collection primeNumbers()
	{
		OutPort outPort = mock(PropertyEditInteractor.OutPort.class);
		RuleModel model = mock(RuleModel.class);

		return Arrays.asList(new Object[][] { 
				{ new PropertyEditInteractor(outPort, model), model } 
		});
	}

	public RuleTesterContractTests(RuleTester tester, RuleModel model)
	{
		_tester = tester;
		_model = model;
	}
	
	@Test
	public void trying_ignore_rules()
	{
		final Iterable<MatchingRule> ignoreRules = new ArrayList<MatchingRule>();
			
		doAnswer(new Answer<Boolean>(){
            @SuppressWarnings("unchecked")
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable
			{
                Object[] args = invocation.getArguments();
				String text = (String) args[0];
				List<MatchingRule> candidates = (List<MatchingRule>) args[1];
				
				if(ignoreRules != candidates) fail("unrecognized list of ignore rules");
				
				if(text.equals("rule-1")) return true;
				if(text.equals("rule-2")) return false;
				
				return false;
			}
			
		}).when(_model).testMatchingRulesWithText(anyString(), anyListOf(MatchingRule.class));

		
		assertThat(_tester.tryMatchingRulesWithText("rule-1", ignoreRules), is(true));
		assertThat(_tester.tryMatchingRulesWithText("rule-2", ignoreRules), is(false));
	}

	@Test
	public void trying_override_rules()
	{
		final Iterable<OverrideRule> overrideRules = new ArrayList<OverrideRule>();
			
		doAnswer(new Answer<String>(){
            @SuppressWarnings("unchecked")
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable
			{
                Object[] args = invocation.getArguments();
				String text = (String) args[0];
				List<OverrideRule> candidates = (List<OverrideRule>) args[1];
				
				if(overrideRules != candidates) fail("unrecognized list of override rules");
				
				if(text.equals("rule-1")) return "override";
	
				return "";
			}
			
		}).when(_model).testOverrideRulesWithText(anyString(), anyListOf(OverrideRule.class));

		assertThat(_tester.tryOverrideRulesWithText("rule-1", overrideRules), is("override"));
		assertThat(_tester.tryOverrideRulesWithText("rule-2", overrideRules), is(""));
	}

}
