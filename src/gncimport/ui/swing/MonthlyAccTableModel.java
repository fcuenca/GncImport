package gncimport.ui.swing;

import gncimport.transfer.MonthlyAccountParam;

import java.util.List;

@SuppressWarnings("serial")
public class MonthlyAccTableModel extends PropertyTableModel
{
	public static final String[] COLUMN_TITLES = { "Order", "Account Name" };
	
	private List<MonthlyAccountParam> _accList;

	public MonthlyAccTableModel(List<MonthlyAccountParam> accList)
	{
		super(COLUMN_TITLES);
		
		if(accList == null) throw new IllegalArgumentException("Account list cannot be null!");
		
		this._accList = accList;
	}

	@Override
	public int getRowCount()
	{
		return _accList.size();
	}
	
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return col != 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		final MonthlyAccountParam row = _accList.get(rowIndex);
		
		return columnIndex == 0 ? row.sequenceNo : row.accName;
	}

	@Override
	public void newRow()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRow(int row)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid()
	{
		// TODO Auto-generated method stub
		return true;
	}

}
