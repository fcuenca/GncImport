package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.UserEnteredMatchingRule;
import gncimport.ui.swing.ValueEditor;

import javax.swing.JTable;
import javax.swing.JTextField;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiTask;
import org.junit.Test;

public class ValueEditorTests
{

	@Test
	public void shows_rule_definition_raw_text_for_edit()
	{
		GuiActionRunner.execute(new GuiTask()
		{
			@Override
			protected void executeInEDT() throws Throwable
			{
				ValueEditor editor = new ValueEditor(UserEnteredMatchingRule.Factory);
				MatchingRule ruleDef = new MatchingRuleForTest("some rule");
				
				JTextField tf = (JTextField)editor.getTableCellEditorComponent(
						new JTable(), ruleDef, true, 0, 1);

				assertThat(tf.getText(), is(ruleDef.text()));
			}
		});
	}
	
	@Test
	public void converts_entered_text_to_rule_definition()
	{
		GuiActionRunner.execute(new GuiTask()
		{
			@Override
			protected void executeInEDT() throws Throwable
			{
				ValueEditor editor = new ValueEditor(UserEnteredMatchingRule.Factory);
				
				JTextField tf = (JTextField)editor.getTableCellEditorComponent(
						new JTable(), new MatchingRuleForTest("some rule"), true, 0, 1);
				
				tf.setText("new rule");
				
				assertThat(editor.getCellEditorValue(), is((Object)new UserEnteredMatchingRule("new rule")));
			}
		});
	}
}
