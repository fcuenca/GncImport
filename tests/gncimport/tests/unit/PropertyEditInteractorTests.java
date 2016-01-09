package gncimport.tests.unit;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.interactors.PropertyEditInteractor;
import gncimport.models.PropertyModel;
import gncimport.transfer.RuleDefinition;

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
	private PropertyModel _model;
	private PropertyEditInteractor _interactor;

	@Captor
	private ArgumentCaptor<List<RuleDefinition>> _expectedList;
	
	@Before
	public void Setup()
	{
		_outPort = mock(PropertyEditInteractor.OutPort.class);
		_model = mock(PropertyModel.class);
		
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
				List<RuleDefinition> rules = (List<RuleDefinition>) args[0];
                rules.addAll(ListUtils.list_of(new RuleDefinitionForTest("rule-1"), new RuleDefinitionForTest("rule-2")));
				return null;
			}
			
		}).when(_model).copyIgnoreRules(anyListOf(RuleDefinition.class));
		
		_interactor.editProperties();
		
		verify(_outPort).editProperties(_expectedList.capture());
		assertThat(_expectedList.getValue(), hasSize(2));
		assertThat(_expectedList.getValue(), hasItems( testRule("rule-1"), testRule("rule-2")));
	}
	
	@Test
	public void updates_edited_properties_when_user_makes_changes()
	{
		List<RuleDefinition> expectedEditedRules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("rule-1"), 
				new RuleDefinitionForTest("rule-2")));
		
		doAnswer(new Answer<Boolean>(){
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable
			{
                Object[] args = invocation.getArguments();
                @SuppressWarnings("unchecked")
				List<RuleDefinition> rules = (List<RuleDefinition>) args[0];
                rules.addAll(ListUtils.list_of(new RuleDefinitionForTest("rule-1"), new RuleDefinitionForTest("rule-2")));
				return true;
			}
			
		}).when(_outPort).editProperties(anyListOf(RuleDefinition.class));
		
		_interactor.editProperties();
		
		verify(_model).replaceIgnoreRules(expectedEditedRules);
	}
	
	@Test
	public void keeps_properties_unchanged_when_user_cancel_edits()
	{
		when(_outPort.editProperties(anyListOf(RuleDefinition.class))).thenReturn(false);
		
		_interactor.editProperties();
		
		verify(_model, never()).replaceIgnoreRules(anyListOf(RuleDefinition.class));
	}
	
	private RuleDefinition testRule(String text)
	{
		return new RuleDefinitionForTest(text);
	}
}
