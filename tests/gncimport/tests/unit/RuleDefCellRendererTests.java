package gncimport.tests.unit;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import gncimport.ui.swing.RuleDefCellRenderer;

import org.junit.Test;

public class RuleDefCellRendererTests
{
	@Test
	public void renders_rule_definition_display_text()
	{
		RuleDefCellRenderer renderer = new RuleDefCellRenderer();
		
		renderer.setValue(new RuleDefinitionForTest("some rule"));
		
		assertThat(renderer.getText(), is("some rule"));
	}

}
