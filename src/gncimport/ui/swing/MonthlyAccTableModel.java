package gncimport.ui.swing;

import gncimport.transfer.MonthlyAccount;
import gncimport.transfer.ScreenValue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("serial")
public class MonthlyAccTableModel extends PropertyTableModel
{
	public static final String[] COLUMN_TITLES = { "Account Name" };
	
	public static final Class<?>[] COLUMN_CLASSES = {  ScreenValue.class };

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
		// List needs to be sorted "in place", otherwise it won't be saved :-/
		Collections.sort(accList, new Comparator<MonthlyAccount>()
		{
			@Override
			public int compare(MonthlyAccount p1, MonthlyAccount p2)
			{
				return new Integer(p1.sequenceNo).compareTo(p2.sequenceNo);
			}
		});
		
		return accList;
	}

	@Override
	public int getRowCount()
	{
		return _accList.size();
	}
	
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}

	@Override
	public Object getValueAt(int rowIndex, int _unused_)
	{
		final MonthlyAccount row = _accList.get(rowIndex);
		
		return row.asScreenValue();
	}

	@Override 
	public void setValueAt(Object aValue, int rowIndex, int _unused_) 
	{
		MonthlyAccount original = _accList.get(rowIndex);
		
		_accList.set(rowIndex, new MonthlyAccount(original.sequenceNo, (String)aValue));
	};
	
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
