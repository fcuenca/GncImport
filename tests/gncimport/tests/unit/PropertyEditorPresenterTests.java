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
import gncimport.transfer.MonthlyAccountParam;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleCategory;
import gncimport.transfer.RuleTester;
import gncimport.ui.TxView;
import gncimport.ui.presenters.PropertyEditorPresenter;
import gncimport.ui.swing.MonthlyAccTableModel;
import gncimport.ui.swing.PropertyTableModel;
import gncimport.ui.swing.TxRuleTableModel;

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
	private ArgumentCaptor<Map<RuleCategory, PropertyTableModel>> expectedModelMap;
	
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
	
		List<OverrideRule> rules3 = new ArrayList<OverrideRule>(ListUtils.list_of(
				new OverrideRule("rule-1", "tx-override-1"), 
				new OverrideRule("rule-2", "tx-override-2"), 
				new OverrideRule("rule-3", "tx-override-3")));
		
		List<MonthlyAccountParam> rules4 = new ArrayList<MonthlyAccountParam>(ListUtils.list_of(
				new MonthlyAccountParam(1, "acc-1"),
				new MonthlyAccountParam(2, "acc-2")));
	
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		allRules.put(RuleCategory.ignore, rules);
		allRules.put(RuleCategory.acc_override, rules2);
		allRules.put(RuleCategory.tx_override, rules3);
		allRules.put(RuleCategory.monthly_accs, rules4);

		when(_view.editProperties(expectedModelMap.capture())).thenReturn(true);
		
		assertThat(_presenter.editProperties(allRules, _tester), is(true));
		
		TxRuleTableModel tm = (TxRuleTableModel) expectedModelMap.getValue().get(RuleCategory.ignore);
		assertThat(tm.getRowCount(), is(2));
		assertThat(tm.getValueAt(0, 0), is((Object)new ScreenValueForTest("rule-1")));
		assertThat(tm.getValueAt(1, 0), is((Object)new ScreenValueForTest("rule-2")));
		
		TxRuleTableModel tm2 = (TxRuleTableModel) expectedModelMap.getValue().get(RuleCategory.acc_override);
		assertThat(tm2.getRowCount(), is(2));
		assertThat(tm2.getValueAt(0, 0), is((Object)new ScreenValueForTest("rule-1")));
		assertThat(tm2.getValueAt(1, 1), is((Object)new ScreenValueForTest("acc-2")));

		TxRuleTableModel tm3 = (TxRuleTableModel) expectedModelMap.getValue().get(RuleCategory.tx_override);
		assertThat(tm3.getRowCount(), is(3));
		assertThat(tm3.getValueAt(0, 0), is((Object)new ScreenValueForTest("rule-1")));
		assertThat(tm3.getValueAt(1, 1), is((Object)new ScreenValueForTest("tx-override-2")));
		assertThat(tm3.getValueAt(2, 1), is((Object)new ScreenValueForTest("tx-override-3")));
		
		MonthlyAccTableModel tm4 = (MonthlyAccTableModel) expectedModelMap.getValue().get(RuleCategory.monthly_accs);
		assertThat(tm4.getRowCount(), is(2));
		assertThat(tm4.getValueAt(0, 0), is((Object)1));
		assertThat(tm4.getValueAt(1, 1), is((Object)(new MonthlyAccountParam(2, "acc-2"))));
	}
	
	@Test
	public void signals_that_user_canceled_editing()
	{
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
		allRules.put(RuleCategory.ignore, new ArrayList<MatchingRule>());
		allRules.put(RuleCategory.acc_override, new ArrayList<OverrideRule>());
		allRules.put(RuleCategory.tx_override, new ArrayList<OverrideRule>());
		allRules.put(RuleCategory.monthly_accs, new ArrayList<MonthlyAccountParam>());

		when(_view.editProperties(anyMapOf(RuleCategory.class, PropertyTableModel.class))).thenReturn(false);
		
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
		allRules.put(RuleCategory.tx_override, new ArrayList<OverrideRule>());
		allRules.put(RuleCategory.monthly_accs, new ArrayList<MonthlyAccountParam>());


		when(_view.editProperties(anyMapOf(RuleCategory.class, PropertyTableModel.class)))
			.thenReturn(true)
			.thenReturn(false);
		
		assertThat(_presenter.editProperties(allRules, _tester), is(false));
		
		verify(_view, times(2)).editProperties(anyMapOf(RuleCategory.class, PropertyTableModel.class));
		verify(_view).displayErrorMessage(anyString());
	}
}
