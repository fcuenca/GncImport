package gncimport.tests.unit;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.transfer.ScreenValue;
import gncimport.ui.swing.ValueRenderer;

import org.junit.Test;

public class ValueRendererTests
{
	@Test
	public void renders_rule_definition_display_text()
	{
		ValueRenderer renderer = new ValueRenderer();
		ScreenValue ruleDef = new ScreenValueForTest("some rule");
		
		renderer.setValue(ruleDef);
		
		assertThat(renderer.getText(), is(ruleDef.displayText()));
	}
	
	@Test
	public void valid_rule_definitions_dont_have_icon_and_tooltip()
	{
		ValueRenderer renderer = new ValueRenderer();
		ScreenValue ruleDef = new ScreenValueForTest("some rule");
		
		renderer.setValue(ruleDef);
		
		assertThat(renderer.getIcon(), is(nullValue()));
		assertThat(renderer.getToolTipText(), is(nullValue()));
	}

	@Test
	public void invalid_rule_definitions_use_hint_as_tooltip()
	{
		ValueRenderer renderer = new ValueRenderer();
		ScreenValue ruleDef = new ScreenValueForTest("some rule", false);
		
		renderer.setValue(ruleDef);
		
		assertThat(renderer.getToolTipText(), is(ruleDef.hint()));
	}
	
	@Test
	public void invalid_rule_definition_renders_icon()
	{
		ValueRenderer renderer = new ValueRenderer();
		ScreenValue ruleDef = new ScreenValueForTest("some rule", false);
		
		renderer.setValue(ruleDef);
		
		assertThat(renderer.getIcon(), is(not(nullValue())));
	}
}
