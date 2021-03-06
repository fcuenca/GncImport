package gncimport.ui.swing;

import gncimport.transfer.ScreenValue;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ValueEditor extends DefaultCellEditor
{
	private static final long serialVersionUID = -5608349352281588071L;
	private ScreenValue _originalValue;

	public ValueEditor()
	{
		super(new JTextField());
		
		JTextField tf = (JTextField)getComponent();
		tf.setBorder(null);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
			int column)
	{
		JTextField tf = (JTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
		_originalValue = (ScreenValue)value;
		
		tf.setText(_originalValue.text());
		return tf;
	}
}