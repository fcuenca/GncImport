package gncimport.ui.swing;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public abstract class PropertyTableModel extends AbstractTableModel
{
	private String[] _columnTitles;
	
	public abstract void newRow();
	public abstract void removeRow(int row);
	public abstract boolean isValid();

	
	protected PropertyTableModel(String[] colTitles)
	{
		_columnTitles = colTitles;
	}
	
	@Override
	public int getColumnCount()
	{
		return _columnTitles.length;
	}
	
	@Override
	public String getColumnName(int col)
	{
		return _columnTitles[col];
	}

}