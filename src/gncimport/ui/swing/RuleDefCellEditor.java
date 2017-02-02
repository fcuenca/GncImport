package gncimport.ui.swing;

import gncimport.transfer.WholeValue;
import gncimport.transfer.WholeValueFactory;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

public class RuleDefCellEditor extends DefaultCellEditor
{
	private static final long serialVersionUID = -5608349352281588071L;
	private WholeValueFactory _factory;

	public RuleDefCellEditor(WholeValueFactory factory)
	{
		super(new JTextField());
		
		_factory = factory;

		JTextField tf = (JTextField)getComponent();
		tf.setBorder(null);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
			int column)
	{
		JTextField tf = (JTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
		WholeValue rule = (WholeValue)value;
		
		tf.setText(rule.text());
		return tf;
	}

	@Override
	public Object getCellEditorValue()
	{
		String theValue = (String)super.getCellEditorValue();
		return _factory.ruleFromText(theValue);
	}
}