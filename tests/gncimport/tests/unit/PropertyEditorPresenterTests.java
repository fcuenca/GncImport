package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleCategory;
import gncimport.transfer.RuleTester;
import gncimport.ui.TxView;
import gncimport.ui.presenters.PropertyEditorPresenter;
import gncimport.ui.swing.RuleTableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private ArgumentCaptor<Map<RuleCategory, RuleTableModel>> expectedModelMap;
	
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
	public void displays_rules()
	{
		List<MatchingRule> rules = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("rule-1"), 
				new MatchingRuleForTest("rule-2")));
		
		List<OverrideRule> rules2 = new ArrayList<OverrideRule>(ListUtils.list_of(
				new OverrideRule("rule-1", "acc-1"), 
				new OverrideRule("rule-2", "acc-2")));
	
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		allRules.put(RuleCategory.ignore, rules);
		allRules.put(RuleCategory.acc_override, rules2);

		when(_view.editProperties(expectedModelMap.capture())).thenReturn(true);
		
		assertThat(_presenter.editProperties(allRules, _tester), is(true));
		
		RuleTableModel tm = expectedModelMap.getValue().get(RuleCategory.ignore);
		
		assertThat(tm.getRowCount(), is(2));
		assertThat(tm.getValueAt(0, 0), is((Object)new MatchingRuleForTest("rule-1")));
		assertThat(tm.getValueAt(1, 0), is((Object)new MatchingRuleForTest("rule-2")));
		
		RuleTableModel tm2 = expectedModelMap.getValue().get(RuleCategory.acc_override);
		
		assertThat(tm2.getRowCount(), is(2));
		assertThat(tm2.getValueAt(0, 0), is((Object)new MatchingRuleForTest("rule-1")));
		assertThat(tm2.getValueAt(1, 1), is((Object)new MatchingRuleForTest("acc-2")));

	}
	
	@Test
	public void signals_that_user_canceled_editing()
	{
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		allRules.put(RuleCategory.ignore, new ArrayList<MatchingRule>());
		allRules.put(RuleCategory.acc_override, new ArrayList<OverrideRule>());
		
		when(_view.editProperties(anyMapOf(RuleCategory.class, RuleTableModel.class))).thenReturn(false);
		
		assertThat(_presenter.editProperties(allRules, _tester), is(false));
	}
	
	@Test
	public void displays_error_if_there_are_invalid_properties()
	{
		List<MatchingRule> rules = new ArrayList<MatchingRule>(ListUtils.list_of(
				 new MatchingRuleForTest("rule-1"), 
				 new MatchingRuleForTest("rule-2", false)));
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		allRules.put(RuleCategory.ignore, rules);
		allRules.put(RuleCategory.acc_override, new ArrayList<OverrideRule>());


		when(_view.editProperties(anyMapOf(RuleCategory.class, RuleTableModel.class)))
			.thenReturn(true)
			.thenReturn(false);
		
		assertThat(_presenter.editProperties(allRules, _tester), is(false));
		
		verify(_view, times(2)).editProperties(anyMapOf(RuleCategory.class, RuleTableModel.class));
		verify(_view).displayErrorMessage(anyString());
	}
}
