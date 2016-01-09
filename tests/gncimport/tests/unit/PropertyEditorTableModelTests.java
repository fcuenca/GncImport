package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.transfer.RuleDefinition;
import gncimport.ui.swing.PropertyEditorTableModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PropertyEditorTableModelTests
{
	@Test
	public void displays_list_of_rule_definitions()
	{
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("rule-1"), 
				new RuleDefinitionForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules);
		
		assertThat(tm.getRowCount(), is(2));
		assertThat(tm.getValueAt(0, 0), is((Object)new RuleDefinitionForTest("rule-1")));
		assertThat(tm.getValueAt(1, 0), is((Object)new RuleDefinitionForTest("rule-2")));
	}
	
	@Test
	public void columns_are_editable()
	{
		PropertyEditorTableModel tm = new PropertyEditorTableModel(new ArrayList<RuleDefinition>());
		
		for(int i = 0; i < PropertyEditorTableModel.COLUMN_TITLES.length; i++)
		{
			assertThat(tm.isCellEditable(1, i), is(true));			
		}
	}
	
	@Test
	public void all_columns_have_title()
	{
		PropertyEditorTableModel tm = new PropertyEditorTableModel(new ArrayList<RuleDefinition>());

		assertThat(tm.getColumnCount(), is(PropertyEditorTableModel.COLUMN_TITLES.length));
	}
	
	@Test
	public void all_columns_have_types_defined()
	{
		PropertyEditorTableModel tm = new PropertyEditorTableModel(new ArrayList<RuleDefinition>());

		assertThat(tm.getColumnCount(), is(PropertyEditorTableModel.COLUMN_CLASSES.length));
	}
	
	@Test
	public void declared_column_types_match_values_returned()
	{
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("some rule")));

		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules);

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
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("rule-1"), 
				new RuleDefinitionForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules);
		
		tm.setValueAt(new RuleDefinitionForTest("new value"), 1, 0);
		assertThat(tm.getValueAt(1, 0), is((Object)new RuleDefinitionForTest("new value")));
	}
	
	@Test
	public void valid_when_all_rules_are_valid()
	{
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("rule-1"), 
				new RuleDefinitionForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules);
		
		assertThat(tm.isValid(), is(true));
	}

	@Test
	public void invalid_when_at_least_one_rule_is_invalid()
	{
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("rule-1"), 
				new RuleDefinitionForTest("rule-2", false), 
				new RuleDefinitionForTest("rule-3")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules);
		
		assertThat(tm.isValid(), is(false));
	}
}