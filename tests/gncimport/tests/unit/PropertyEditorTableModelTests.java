package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.RuleTester;
import gncimport.ui.swing.PropertyEditorTableModel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.junit.Before;
import org.junit.Test;

public class PropertyEditorTableModelTests
{
	private RuleTester _tester;
	private List<MatchingRule> _ignoreList;
	private PropertyEditorTableModel _tableModel;
	
	@Before
	public void Setup()
	{
		_tester = mock(RuleTester.class);
		
		_ignoreList = new ArrayList<MatchingRule>(ListUtils.list_of(
				new MatchingRuleForTest("rule-1"), 
				new MatchingRuleForTest("rule-2")));
		
		_tableModel = new PropertyEditorTableModel(_ignoreList, _tester);		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ignore_list_cannot_be_null()
	{
		new PropertyEditorTableModel(null, _tester);	
	}
	
	@Test
	public void displays_list_of_rule_definitions()
	{		
		assertThat(_tableModel.getRowCount(), is(2));
		assertThat(_tableModel.getValueAt(0, 0), is((Object)new MatchingRuleForTest("rule-1")));
		assertThat(_tableModel.getValueAt(1, 0), is((Object)new MatchingRuleForTest("rule-2")));
	}
	
	@Test
	public void columns_are_editable()
	{		
		for(int i = 0; i < PropertyEditorTableModel.COLUMN_TITLES.length; i++)
		{
			assertThat(_tableModel.isCellEditable(1, i), is(true));			
		}
	}
	
	@Test
	public void all_columns_have_title()
	{
		assertThat(_tableModel.getColumnCount(), is(PropertyEditorTableModel.COLUMN_TITLES.length));
	}
	
	@Test
	public void all_columns_have_types_defined()
	{
		assertThat(_tableModel.getColumnCount(), is(PropertyEditorTableModel.COLUMN_CLASSES.length));
	}
	
	@Test
	public void declared_column_types_match_values_returned()
	{
		for(int i = 0; i < PropertyEditorTableModel.COLUMN_CLASSES.length; i++)
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
		assertThat(_tableModel.getValueAt(1, 0), is((Object)new MatchingRuleForTest("new value")));
	}
	
	@Test
	public void valid_when_all_rules_are_valid()
	{		
		assertThat(_tableModel.isValid(), is(true));
	}

	@Test
	public void invalid_when_at_least_one_rule_is_invalid()
	{
		_ignoreList.add(new MatchingRuleForTest("rule-2", false));
				
		assertThat(_tableModel.isValid(), is(false));
	}
	
	@Test
	public void rules_can_be_appended()
	{		
		_tableModel.newRow();
		
		assertThat(_tableModel.getRowCount(), is(3));
		assertThat(_tableModel.getValueAt(2, 0), is((Object)new MatchingRuleForTest("", false)));
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
		_tableModel.removeRule(1);		
		assertThat(_tableModel.getRowCount(), is(1));
		assertThat(_tableModel.getValueAt(0, 0), is((Object)new MatchingRuleForTest("rule-1")));

		_tableModel.removeRule(0);		
		assertThat(_tableModel.getRowCount(), is(0));
	}

	@Test
	public void removing_rules_out_of_bounds_is_ignored()
	{
		_tableModel.removeRule(-1);
		_tableModel.removeRule(2);
		
		assertThat(_tableModel.getRowCount(), is(2));
	}

	@Test
	public void removing_rule_notifies_listeners()
	{
		TableModelListener listener = mock(TableModelListener.class);
		_tableModel.addTableModelListener(listener);
		
		_tableModel.removeRule(0);
		
		verify(listener).tableChanged(any(TableModelEvent.class));
	}
		
	@Test
	public void can_test_rules_against_sample_text()
	{
		when(_tester.tryRulesWithText("rule-1", _ignoreList)).thenReturn(true);
		
		assertThat(_tableModel.testRulesWithText("rule-1"), is(true));
	}
}
