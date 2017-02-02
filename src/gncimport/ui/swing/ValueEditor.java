package gncimport.ui.swing;

import gncimport.transfer.ScreenValue;
import gncimport.transfer.ScreenValueFactory;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ValueEditor extends DefaultCellEditor
{
	private static final long serialVersionUID = -5608349352281588071L;
	private ScreenValueFactory _factory;

	public ValueEditor(ScreenValueFactory factory)
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
		ScreenValue rule = (ScreenValue)value;
		
		tf.setText(rule.text());
		return tf;
	}

	@Override
	public Object getCellEditorValue()
	{
		String theValue = (String)super.getCellEditorValue();
		return _factory.newDomainObjectFromText(theValue);
	}
}