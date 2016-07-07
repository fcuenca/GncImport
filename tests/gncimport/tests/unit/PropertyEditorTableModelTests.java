package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.transfer.MatchingText;
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
	
	@Before
	public void Setup()
	{
		_tester = mock(RuleTester.class);
	}

	@Test
	public void displays_list_of_rule_definitions()
	{
		List<MatchingText> rules = new ArrayList<MatchingText>(ListUtils.list_of(
				new MatchingTextForTest("rule-1"), 
				new MatchingTextForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules, _tester);
		
		assertThat(tm.getRowCount(), is(2));
		assertThat(tm.getValueAt(0, 0), is((Object)new MatchingTextForTest("rule-1")));
		assertThat(tm.getValueAt(1, 0), is((Object)new MatchingTextForTest("rule-2")));
	}
	
	@Test
	public void columns_are_editable()
	{
		PropertyEditorTableModel tm = new PropertyEditorTableModel(new ArrayList<MatchingText>(), _tester);
		
		for(int i = 0; i < PropertyEditorTableModel.COLUMN_TITLES.length; i++)
		{
			assertThat(tm.isCellEditable(1, i), is(true));			
		}
	}
	
	@Test
	public void all_columns_have_title()
	{
		PropertyEditorTableModel tm = new PropertyEditorTableModel(new ArrayList<MatchingText>(), _tester);

		assertThat(tm.getColumnCount(), is(PropertyEditorTableModel.COLUMN_TITLES.length));
	}
	
	@Test
	public void all_columns_have_types_defined()
	{
		PropertyEditorTableModel tm = new PropertyEditorTableModel(new ArrayList<MatchingText>(), _tester);

		assertThat(tm.getColumnCount(), is(PropertyEditorTableModel.COLUMN_CLASSES.length));
	}
	
	@Test
	public void declared_column_types_match_values_returned()
	{
		List<MatchingText> rules = new ArrayList<MatchingText>(ListUtils.list_of(
				new MatchingTextForTest("some rule")));

		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules, _tester);

		for(int i = 0; i < PropertyEditorTableModel.COLUMN_CLASSES.length; i++)
		{
			Class<?> actual = tm.getValueAt(0, i).getClass();
			Class<?> declared = tm.getColumnClass(i);
			
			assertThat(String.format("declared class for column %d (%s) is not compatible actual class (%s)", i, declared, actual),
					declared.isAssignableFrom(actual), is(true));
			
			assertThat("declared class for column " + i + " can not be Object",
					declared.equals(Object.class), is(false));
		}
	}

	@Test
	public void updates_rule_definitions()
	{
		List<MatchingText> rules = new ArrayList<MatchingText>(ListUtils.list_of(
				new MatchingTextForTest("rule-1"), 
				new MatchingTextForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules, _tester);
		
		tm.setValueAt(new MatchingTextForTest("new value"), 1, 0);
		assertThat(tm.getValueAt(1, 0), is((Object)new MatchingTextForTest("new value")));
	}
	
	@Test
	public void valid_when_all_rules_are_valid()
	{
		List<MatchingText> rules = new ArrayList<MatchingText>(ListUtils.list_of(
				new MatchingTextForTest("rule-1"), 
				new MatchingTextForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules, _tester);
		
		assertThat(tm.isValid(), is(true));
	}

	@Test
	public void invalid_when_at_least_one_rule_is_invalid()
	{
		List<MatchingText> rules = new ArrayList<MatchingText>(ListUtils.list_of(
				new MatchingTextForTest("rule-1"), 
				new MatchingTextForTest("rule-2", false), 
				new MatchingTextForTest("rule-3")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules, _tester);
		
		assertThat(tm.isValid(), is(false));
	}
	
	@Test
	public void rules_can_be_appended()
	{
		List<MatchingText> rules = new ArrayList<MatchingText>(ListUtils.list_of(
				new MatchingTextForTest("rule-1"), 
				new MatchingTextForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules, _tester);
		
		tm.newRow();
		
		assertThat(tm.getRowCount(), is(3));
		assertThat(tm.getValueAt(2, 0), is((Object)new MatchingTextForTest("", false)));
	}
	
	@Test
	public void adding_rule_notifies_listeners()
	{
		List<MatchingText> rules = new ArrayList<MatchingText>(ListUtils.list_of(
				new MatchingTextForTest("rule-1"), 
				new MatchingTextForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules, _tester);
		
		TableModelListener listener = mock(TableModelListener.class);
		tm.addTableModelListener(listener);
		
		tm.newRow();
		
		verify(listener).tableChanged(any(TableModelEvent.class));
	}

	@Test
	public void rules_can_be_removed()
	{
		List<MatchingText> rules = new ArrayList<MatchingText>(ListUtils.list_of(
				new MatchingTextForTest("rule-1"), 
				new MatchingTextForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules, _tester);
		
		tm.removeRule(1);		
		assertThat(tm.getRowCount(), is(1));
		assertThat(tm.getValueAt(0, 0), is((Object)new MatchingTextForTest("rule-1")));

		tm.removeRule(0);		
		assertThat(tm.getRowCount(), is(0));
	}

	@Test
	public void removing_rules_out_of_bounds_is_ignored()
	{
		List<MatchingText> rules = new ArrayList<MatchingText>(ListUtils.list_of(
				new MatchingTextForTest("rule-1"), 
				new MatchingTextForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules, _tester);
		
		tm.removeRule(-1);
		tm.removeRule(2);
		
		assertThat(tm.getRowCount(), is(2));
	}

	@Test
	public void removing_rule_notifies_listeners()
	{
		List<MatchingText> rules = new ArrayList<MatchingText>(ListUtils.list_of(
				new MatchingTextForTest("rule-1"), 
				new MatchingTextForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules, _tester);
		
		TableModelListener listener = mock(TableModelListener.class);
		tm.addTableModelListener(listener);
		
		tm.removeRule(0);
		
		verify(listener).tableChanged(any(TableModelEvent.class));
	}
		
	@Test
	public void can_test_rules_against_sample_text()
	{
		List<MatchingText> rules = new ArrayList<MatchingText>(ListUtils.list_of(
				new MatchingTextForTest("rule-1"), 
				new MatchingTextForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules, _tester);
		
		when(_tester.tryRulesWithText("rule-1", rules)).thenReturn(true);
		
		assertThat(tm.testRulesWithText("rule-1"), is(true));
		
	}
}
