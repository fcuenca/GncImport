package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import gncimport.transfer.RuleDefinition;
import gncimport.ui.swing.PropertyEditorTableModel;

import org.junit.Test;

public class PropertyEditorTableModelTests
{

	@Test
	public void contains_single_editable_columns()
	{
		PropertyEditorTableModel tm = new PropertyEditorTableModel(new ArrayList<RuleDefinition>());
		
		assertThat(tm.getColumnCount(), is(1));
		assertThat(tm.isCellEditable(1, 0), is(true));
	}

	@Test
	public void displays_list_of_rule_definitions()
	{
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("rule-1"), 
				new RuleDefinitionForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules);
		
		assertThat(tm.getRowCount(), is(2));
		assertThat(tm.getValueAt(0, 0), is((Object)"rule-1"));
		assertThat(tm.getValueAt(1, 0), is((Object)"rule-2"));
	}
	
	@Test
	public void updates_rule_definitions()
	{
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>(ListUtils.list_of(
				new RuleDefinitionForTest("rule-1"), 
				new RuleDefinitionForTest("rule-2")));
		
		PropertyEditorTableModel tm = new PropertyEditorTableModel(rules);
		
		tm.setValueAt("new value", 1, 0);
		assertThat(tm.getValueAt(1, 0), is((Object)"new value"));
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
