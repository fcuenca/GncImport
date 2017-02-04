package gncimport.ui.swing;

import gncimport.transfer.MonthlyAccount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("serial")
public class MonthlyAccTableModel extends PropertyTableModel
{
	public static final String[] COLUMN_TITLES = { "Order", "Account Name" };
	
	public static final Class<?>[] COLUMN_CLASSES = { Object.class,  MonthlyAccount.class };

	@Override
	public Class<?> getColumnClass(int col)
	{
		return COLUMN_CLASSES[col];
	}

	
	private List<MonthlyAccount> _accList;

	public MonthlyAccTableModel(List<MonthlyAccount> accList)
	{
		super(COLUMN_TITLES);
		
		if(accList == null) throw new IllegalArgumentException("Account list cannot be null!");
		
		_accList = sortedAccountList(accList);
	}

	private List<MonthlyAccount> sortedAccountList(List<MonthlyAccount> accList)
	{
		List<MonthlyAccount> result = new ArrayList<MonthlyAccount>(accList);
		Collections.sort(result, new Comparator<MonthlyAccount>()
		{
			@Override
			public int compare(MonthlyAccount p1, MonthlyAccount p2)
			{
				return new Integer(p1.sequenceNo).compareTo(p2.sequenceNo);
			}
		});
		
		return result;
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
		final MonthlyAccount row = _accList.get(rowIndex);
		
		return columnIndex == 0 ? row.sequenceNo : row;
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
