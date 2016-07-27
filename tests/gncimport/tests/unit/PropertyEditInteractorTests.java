package gncimport.tests.unit;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyListOf;
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
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;

import java.util.ArrayList;
import java.util.List;

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
	private ArgumentCaptor<List<MatchingRule>> _expectedList;
	@Captor
	private ArgumentCaptor<List<OverrideRule>> _expectedAccOverrideList;
	
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
				List<MatchingRule> rules = (List<MatchingRule>) args[0];
                rules.addAll(ListUtils.list_of(new MatchingRuleForTest("rule-1"), new MatchingRuleForTest("rule-2")));
				return null;
			}
			
		}).when(_model).copyRulesTo(anyListOf(MatchingRule.class));
		
		_interactor.editProperties();
		
		verify(_outPort).editProperties(_expectedList.capture(), _expectedAccOverrideList.capture(), same(_interactor));
		assertThat(_expectedList.getValue(), hasSize(2));
		assertThat(_expectedList.getValue(), hasItems( testRule("rule-1"), testRule("rule-2")));
	}
	
	@Test
	public void updates_edited_properties_when_user_makes_changes()
	{
		List<MatchingRule> expectedEditedRules = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("rule-1"), 
				new MatchingRuleForTest("rule-2")));
		
		doAnswer(new Answer<Boolean>(){
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable
			{
                Object[] args = invocation.getArguments();
                @SuppressWarnings("unchecked")
				List<MatchingRule> rules = (List<MatchingRule>) args[0];
                rules.addAll(ListUtils.list_of(new MatchingRuleForTest("rule-1"), new MatchingRuleForTest("rule-2")));
				return true;
			}
			
		}).when(_outPort).editProperties(anyListOf(MatchingRule.class), anyListOf(OverrideRule.class), same(_interactor));
		
		_interactor.editProperties();
		
		verify(_model).replaceRulesWith(expectedEditedRules);
	}
	
	@Test
	public void keeps_properties_unchanged_when_user_cancel_edits()
	{
		when(_outPort.editProperties(anyListOf(MatchingRule.class), anyListOf(OverrideRule.class), same(_interactor))).thenReturn(false);
		
		_interactor.editProperties();
		
		verify(_model, never()).replaceRulesWith(anyListOf(MatchingRule.class));
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
			
		}).when(_model).testRulesWithText(anyString(), anyListOf(MatchingRule.class));
		
		RuleTester tester = _interactor;
		
		assertThat(tester.tryRulesWithText("rule-1", rules), is(true));
		assertThat(tester.tryRulesWithText("rule-2", rules), is(false));
	}

	private MatchingRule testRule(String text)
	{
		return new MatchingRuleForTest(text);
	}
}
