package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;
import gncimport.transfer.TransactionRule;
import gncimport.ui.swing.OverrideRuleTableModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;


public class OverrideRuleTableModelTests
{
	private OverrideRuleTableModel _tableModel;
	private List<OverrideRule> _overrideList;
	private RuleTester _tester;
	
	@SuppressWarnings("serial")
	static class TableModelForTesting extends OverrideRuleTableModel
	{
		public static final String[] COLUMN_TITLES = { "Col 1", "Col 2" };

		public TableModelForTesting(List<? extends TransactionRule> rules, RuleTester tester)
		{
			super(COLUMN_TITLES, rules, tester);
		}
		
	}

	@Before
	public void SetUp()
	{
		_tester = mock(RuleTester.class);

		_overrideList = new ArrayList<OverrideRule>(ListUtils.list_of(
				new OverrideRule("desc-1", "acc-1"),
				new OverrideRule("desc-2", "acc-2")));
		
		_tableModel = new TableModelForTesting(_overrideList, _tester);		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void override_list_cannot_be_null()
	{
		new TableModelForTesting(null, _tester);	
	}
	
	@Test
	public void displays_list_of_overrides()
	{		
		assertThat(_tableModel.getRowCount(), is(2));
		
		assertThat(_tableModel.getValueAt(0, 0), is((Object)new MatchingRuleForTest("desc-1")));
		assertThat(_tableModel.getValueAt(0, 1), is((Object)new MatchingRuleForTest("acc-1")));

		assertThat(_tableModel.getValueAt(1, 0), is((Object)new MatchingRuleForTest("desc-2")));
		assertThat(_tableModel.getValueAt(1, 1), is((Object)new MatchingRuleForTest("acc-2")));
	}

	@Test
	public void columns_are_editable()
	{		
		for(int i = 0; i < TableModelForTesting.COLUMN_TITLES.length; i++)
		{
			assertThat(_tableModel.isCellEditable(1, i), is(true));			
		}
	}

	@Test
	public void all_columns_have_title()
	{
		assertThat(_tableModel.getColumnCount(), is(TableModelForTesting.COLUMN_TITLES.length));
	}
	
	@Test
	public void all_columns_have_types_defined()
	{
		assertThat(_tableModel.getColumnCount(), is(TableModelForTesting.COLUMN_CLASSES.length));
	}
	
	@Test
	public void declared_column_types_match_values_returned()
	{
		for(int i = 0; i < TableModelForTesting.COLUMN_CLASSES.length; i++)
		{
			Class<?> actual = _tableModel.getValueAt(0, i).getClass();
			Class<?> declared = _tableModel.getColumnClass(i);
			
			assertThat(String.format("declared class for column %d (%s) is not compatible actual class (%s)", i, declared, actual),
					declared.isAssignableFrom(actual), is(true));
			
			assertThat("declared class for column " + i + " can not be Object",
					declared.equals(Object.class), is(false));
		}
	}

	@Test
	public void updates_rule_definitions()
	{		
		_tableModel.setValueAt(new MatchingRuleForTest("new value"), 1, 0);
		_tableModel.setValueAt(new MatchingRuleForTest("new acc"), 0, 1);
		
		assertThat(_tableModel.getValueAt(1, 0), is((Object)new MatchingRuleForTest("new value")));
		assertThat(_tableModel.getValueAt(1, 1), is((Object)new MatchingRuleForTest("acc-2")));		

		assertThat(_tableModel.getValueAt(0, 0), is((Object)new MatchingRuleForTest("desc-1")));
		assertThat(_tableModel.getValueAt(0, 1), is((Object)new MatchingRuleForTest("new acc")));		
	}
	
  	@Test
	public void valid_when_all_rules_are_valid()
	{		
		assertThat(_tableModel.isValid(), is(true));
	}

	@Test
	public void invalid_when_at_least_one_rule_is_invalid()
	{
		_overrideList.add(new OverrideRule(new MatchingRuleForTest("invalid", false), new MatchingRuleForTest("valid")));
				
		assertThat(_tableModel.isValid(), is(false));
	}
	
	@Test
	public void rules_can_be_appended()
	{		
		_tableModel.newRow();
		
		assertThat(_tableModel.getRowCount(), is(3));
		assertThat(_tableModel.getValueAt(2, 0), is((Object)new MatchingRuleForTest("", false)));
		assertThat(_tableModel.getValueAt(2, 1), is((Object)new MatchingRuleForTest("", false)));
	}
	
	@Test
	public void adding_rule_notifies_listeners()
	{
		TableModelListener listener = mock(TableModelListener.class);
		_tableModel.addTableModelListener(listener);
		
		_tableModel.newRow();
		
		verify(listener).tableChanged(any(TableModelEvent.class));
	}

	@Test
	public void rules_can_be_removed()
	{
		_tableModel.removeRow(1);		
		assertThat(_tableModel.getRowCount(), is(1));
		assertThat(_tableModel.getValueAt(0, 1), is((Object)new MatchingRuleForTest("acc-1")));

		_tableModel.removeRow(0);		
		assertThat(_tableModel.getRowCount(), is(0));
	}

	@Test
	public void removing_rules_out_of_bounds_is_ignored()
	{
		_tableModel.removeRow(-1);
		_tableModel.removeRow(2);
		
		assertThat(_tableModel.getRowCount(), is(2));
	}

	@Test
	public void removing_rule_notifies_listeners()
	{
		TableModelListener listener = mock(TableModelListener.class);
		_tableModel.addTableModelListener(listener);
		
		_tableModel.removeRow(0);
		
		verify(listener).tableChanged(any(TableModelEvent.class));
	}
	
	@Test
	public void can_test_rules_against_sample_text()
	{
		when(_tester.tryRulesWithText("rule-1", _overrideList)).thenReturn("override");
		
		assertThat(_tableModel.testRulesWithText("rule-1"), is("override"));
	}

}
