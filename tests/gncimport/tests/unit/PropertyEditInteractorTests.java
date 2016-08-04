package gncimport.tests.unit;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.interactors.PropertyEditInteractor;
import gncimport.models.RuleModel;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.RuleTester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class PropertyEditInteractorTests
{
	private PropertyEditInteractor.OutPort _outPort;
	private RuleModel _model;
	private PropertyEditInteractor _interactor;

	@Captor
	private ArgumentCaptor<Map<String, Object>> _expectedRules;
	
	@Before
	public void Setup()
	{
		_outPort = mock(PropertyEditInteractor.OutPort.class);
		_model = mock(RuleModel.class);
		
		_interactor = new PropertyEditInteractor(_outPort, _model);		
	}
	
	@Test
	public void displays_current_properties()
	{	
		doAnswer(new Answer<Void>(){
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable
			{
                Object[] args = invocation.getArguments();
                @SuppressWarnings("unchecked")
				Map<String, Object> allRules = (Map<String, Object>) args[0];
                allRules.put("first", "list of rules 1");
                allRules.put("second", "list of rules 2");
                
				return null;
			}
			
		}).when(_model).copyRulesTo(anyMapOf(String.class, Object.class));
		
		_interactor.editProperties();
		
		verify(_outPort).editProperties(_expectedRules.capture(), same(_interactor));
		
		assertThat(_expectedRules.getValue().size(), is(2));
		assertThat(_expectedRules.getValue(), hasEntry("first", (Object)"list of rules 1"));
		assertThat(_expectedRules.getValue(), hasEntry("second", (Object)"list of rules 2"));
	}
	
	@Test
	public void updates_edited_properties_when_user_makes_changes()
	{
		final Map<String, Object> expectedRuleMap = new HashMap<String, Object>();
		expectedRuleMap.put("first", "edited list of rules 2");
		expectedRuleMap.put("second", "edited list of rules 2");
		
		doAnswer(new Answer<Boolean>(){
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable
			{
                Object[] args = invocation.getArguments();
                
                @SuppressWarnings("unchecked")
				Map<String, Object> allRules = (Map<String, Object>) args[0];
                allRules.putAll(expectedRuleMap);
                
				return true;
			}
			
		}).when(_outPort).editProperties(anyMapOf(String.class, Object.class), same(_interactor));
		
		_interactor.editProperties();
		
		verify(_model).replaceRulesWith(expectedRuleMap);
	}
	
	@Test
	public void keeps_properties_unchanged_when_user_cancel_edits()
	{
		when(_outPort.editProperties(anyMapOf(String.class, Object.class), same(_interactor))).thenReturn(false);
		
		_interactor.editProperties();
		
		verify(_model, never()).replaceRulesWith(anyMapOf(String.class, Object.class));
	}
	
	@Test
	public void implements_rule_tester_interface()
	{
		final Iterable<MatchingRule> rules = new ArrayList<MatchingRule>();
			
		doAnswer(new Answer<Boolean>(){
            @SuppressWarnings("unchecked")
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable
			{
                Object[] args = invocation.getArguments();
				String text = (String) args[0];
				List<MatchingRule> candidates = (List<MatchingRule>) args[1];
				
				if(rules != candidates) fail("unrecognized list of rules");
				
				if(text.equals("rule-1")) return true;
				if(text.equals("rule-2")) return false;
				
				return false;
			}
			
		}).when(_model).testMatchingRulesWithText(anyString(), anyListOf(MatchingRule.class));
		
		RuleTester tester = _interactor;
		
		assertThat(tester.tryRulesWithText("rule-1", rules), is(true));
		assertThat(tester.tryRulesWithText("rule-2", rules), is(false));
	}
}
