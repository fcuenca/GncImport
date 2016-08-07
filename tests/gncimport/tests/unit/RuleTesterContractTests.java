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
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;
import gncimport.transfer.TransactionRule;

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
	
	class TransactionRuleForTest implements TransactionRule
	{
		@Override
		public boolean isValid()
		{
			return true;
		}

		@Override
		public boolean matches(String someText)
		{
			return true;
		}

		@Override
		public String textForPossitiveMatch()
		{
			return "matches";
		}	
	}
	
	@Test
	public void trying_rules_to_see_if_they_match()
	{
		final Iterable<TransactionRuleForTest> someRules = new ArrayList<TransactionRuleForTest>();
			
		doAnswer(new Answer<String>(){
            @SuppressWarnings("unchecked")
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable
			{
                Object[] args = invocation.getArguments();
                
				String actualText = (String) args[0];
				List<TransactionRuleForTest> actualRules = (List<TransactionRuleForTest>) args[1];
				
				if(someRules != actualRules) fail("unrecognized list of override rules");	
				if(actualText.equals("rule-1")) return "override";
	
				return "";
			}
			
		}).when(_model).testRulesWithText(anyString(), anyListOf(OverrideRule.class));

		assertThat(_tester.tryRulesWithText("rule-1", someRules), is("override"));
		assertThat(_tester.tryRulesWithText("rule-2", someRules), is(""));
	}

}
