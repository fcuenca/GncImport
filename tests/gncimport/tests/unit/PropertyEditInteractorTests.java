package gncimport.tests.unit;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Matchers.anyListOf;
import gncimport.interactors.PropertyEditInteractor;
import gncimport.models.PropertyModel;

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
	private ArgumentCaptor<List<String>> _expectedList;
	
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
				List<String> rules = (List<String>) args[0];
                rules.addAll(ListUtils.list_of("rule-1", "rule-2"));
				return null;
			}
			
		}).when(_model).copyIgnoreRules(anyListOf(String.class));
		
		_interactor.displayCurrentConfig();
		
		verify(_outPort).editProperties(_expectedList.capture());
		assertThat(_expectedList.getValue(), hasSize(2));
		assertThat(_expectedList.getValue(), hasItems("rule-1", "rule-2"));
	}
	
	@Test
	public void updates_edited_properties()
	{
		List<String> newRules = ListUtils.list_of("rule-1", "rule-2");
		
		_interactor.updateProperties(newRules);
		
		verify(_model).replaceIgnoreRules(newRules);
	}
}
