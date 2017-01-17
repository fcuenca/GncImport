package gncimport.ui.swing;

import gncimport.transfer.MonthlyAccountParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
		
		_accList = sortedAccountList(accList);
	}

	private List<MonthlyAccountParam> sortedAccountList(List<MonthlyAccountParam> accList)
	{
		List<MonthlyAccountParam> result = new ArrayList<MonthlyAccountParam>(accList);
		Collections.sort(result, new Comparator<MonthlyAccountParam>()
		{
			@Override
			public int compare(MonthlyAccountParam p1, MonthlyAccountParam p2)
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
