package gncimport.tests.unit;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.interactors.PropertyEditInteractor;
import gncimport.models.RuleModel;
import gncimport.transfer.RuleCategory;

import java.util.HashMap;
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
	private ArgumentCaptor<Map<RuleCategory, Object>> _expectedRules;
	
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
				Map<RuleCategory, Object> allRules = (Map<RuleCategory, Object>) args[0];
                allRules.put(RuleCategory.ignore, "list of rules 1");
                allRules.put(RuleCategory.acc_override, "list of rules 2");
                
				return null;
			}
			
		}).when(_model).copyRulesTo(anyMapOf(RuleCategory.class, Object.class));
		
		_interactor.editProperties();
		
		verify(_outPort).editProperties(_expectedRules.capture(), same(_interactor));
		
		assertThat(_expectedRules.getValue().size(), is(2));
		assertThat(_expectedRules.getValue(), hasEntry(RuleCategory.ignore, (Object)"list of rules 1"));
		assertThat(_expectedRules.getValue(), hasEntry(RuleCategory.acc_override, (Object)"list of rules 2"));
	}
	
	@Test
	public void updates_edited_properties_when_user_makes_changes()
	{
		final Map<RuleCategory, Object> expectedRuleMap = new HashMap<RuleCategory, Object>();
		expectedRuleMap.put(RuleCategory.ignore, "edited list of rules 2");
		expectedRuleMap.put(RuleCategory.acc_override, "edited list of rules 2");
		
		doAnswer(new Answer<Boolean>(){
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable
			{
                Object[] args = invocation.getArguments();
                
                @SuppressWarnings("unchecked")
				Map<RuleCategory, Object> allRules = (Map<RuleCategory, Object>) args[0];
                allRules.putAll(expectedRuleMap);
                
				return true;
			}
			
		}).when(_outPort).editProperties(anyMapOf(RuleCategory.class, Object.class), same(_interactor));
		
		_interactor.editProperties();
		
		verify(_model).replaceRulesWith(expectedRuleMap);
	}
	
	@Test
	public void keeps_properties_unchanged_when_user_cancel_edits()
	{
		when(_outPort.editProperties(anyMapOf(RuleCategory.class, Object.class), same(_interactor))).thenReturn(false);
		
		_interactor.editProperties();
		
		verify(_model, never()).replaceRulesWith(anyMapOf(RuleCategory.class, Object.class));
	}
	
}
