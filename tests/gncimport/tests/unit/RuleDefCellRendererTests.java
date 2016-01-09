package gncimport.tests.unit;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import gncimport.transfer.RuleDefinition;
import gncimport.ui.swing.RuleDefCellRenderer;

import org.junit.Test;

public class RuleDefCellRendererTests
{
	@Test
	public void renders_rule_definition_display_text()
	{
		RuleDefCellRenderer renderer = new RuleDefCellRenderer();
		RuleDefinition ruleDef = new RuleDefinitionForTest("some rule");
		
		renderer.setValue(ruleDef);
		
		assertThat(renderer.getText(), is(ruleDef.displayText()));
	}
}
