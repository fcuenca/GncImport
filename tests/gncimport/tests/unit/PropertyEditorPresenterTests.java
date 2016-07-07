package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.RuleTester;
import gncimport.ui.TxView;
import gncimport.ui.presenters.PropertyEditorPresenter;
import gncimport.ui.swing.PropertyEditorTableModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PropertyEditorPresenterTests
{
	@Captor
	private ArgumentCaptor<PropertyEditorTableModel> expectedTableModel;
	
	private TxView _view;
	private PropertyEditorPresenter _presenter;
	private RuleTester _tester;

	@Before
	public void Setup()
	{
		_view = mock(TxView.class);
		_presenter = new PropertyEditorPresenter(_view);
		_tester = mock(RuleTester.class);
	}
	
	@Test
	public void displays_ignore_rules()
	{
		List<MatchingRule> rules = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("rule-1"), 
				new MatchingRuleForTest("rule-2")));
		
		when(_view.editProperties(expectedTableModel.capture())).thenReturn(true);
		
		assertThat(_presenter.editProperties(rules, _tester), is(true));
		
		PropertyEditorTableModel tm = expectedTableModel.getValue();
		
		assertThat(tm.getRowCount(), is(2));
		assertThat(tm.getValueAt(0, 0), is((Object)new MatchingRuleForTest("rule-1")));
		assertThat(tm.getValueAt(1, 0), is((Object)new MatchingRuleForTest("rule-2")));
	}
	
	@Test
	public void signals_that_user_canceled_editing()
	{
		when(_view.editProperties(any(PropertyEditorTableModel.class))).thenReturn(false);
		
		assertThat(_presenter.editProperties(new ArrayList<MatchingRule>(), _tester), is(false));
	}
	
	@Test
	public void displays_error_if_there_are_invalid_properties()
	{
		List<MatchingRule> rules = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("rule-1"), 
				new MatchingRuleForTest("rule-2", false)));
		
		when(_view.editProperties(any(PropertyEditorTableModel.class)))
			.thenReturn(true)
			.thenReturn(false);
		
		assertThat(_presenter.editProperties(rules, _tester), is(false));
		
		verify(_view, times(2)).editProperties(any(PropertyEditorTableModel.class));
		verify(_view).displayErrorMessage(anyString());
	}
}
