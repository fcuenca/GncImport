package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.transfer.EditableMatchingRule;
import gncimport.transfer.ScreenValue;
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
				ValueEditor editor = new ValueEditor(EditableMatchingRule.Factory);
				ScreenValue valueToEdit = new ScreenValueForTest("some rule");
				
				JTextField tf = (JTextField)editor.getTableCellEditorComponent(
						new JTable(), valueToEdit, true, 0, 1);

				assertThat(tf.getText(), is(valueToEdit.text()));
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
				ValueEditor editor = new ValueEditor(EditableMatchingRule.Factory);
				
				JTextField tf = (JTextField)editor.getTableCellEditorComponent(
						new JTable(), new ScreenValueForTest("some rule"), true, 0, 1);
				
				tf.setText("new rule");
				
				assertThat(editor.getCellEditorValue(), is((Object)new EditableMatchingRule("new rule")));
			}
		});
	}
}
