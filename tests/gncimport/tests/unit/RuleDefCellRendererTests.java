package gncimport.tests.unit;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.transfer.MatchingText;
import gncimport.ui.swing.RuleDefCellRenderer;

import org.junit.Test;

public class RuleDefCellRendererTests
{
	@Test
	public void renders_rule_definition_display_text()
	{
		RuleDefCellRenderer renderer = new RuleDefCellRenderer();
		MatchingText ruleDef = new MatchingTextForTest("some rule");
		
		renderer.setValue(ruleDef);
		
		assertThat(renderer.getText(), is(ruleDef.displayText()));
	}
	
	@Test
	public void valid_rule_definitions_dont_have_icon_and_tooltip()
	{
		RuleDefCellRenderer renderer = new RuleDefCellRenderer();
		MatchingText ruleDef = new MatchingTextForTest("some rule");
		
		renderer.setValue(ruleDef);
		
		assertThat(renderer.getIcon(), is(nullValue()));
		assertThat(renderer.getToolTipText(), is(nullValue()));
	}

	@Test
	public void invalid_rule_definitions_use_hint_as_tooltip()
	{
		RuleDefCellRenderer renderer = new RuleDefCellRenderer();
		MatchingText ruleDef = new MatchingTextForTest("some rule", false);
		
		renderer.setValue(ruleDef);
		
		assertThat(renderer.getToolTipText(), is(ruleDef.hint()));
	}
	
	@Test
	public void invalid_rule_definition_renders_icon()
	{
		RuleDefCellRenderer renderer = new RuleDefCellRenderer();
		MatchingText ruleDef = new MatchingTextForTest("some rule", false);
		
		renderer.setValue(ruleDef);
		
		assertThat(renderer.getIcon(), is(not(nullValue())));
	}
}
