package gncimport.ui.swing;

import gncimport.transfer.MonthlyAccount;
import gncimport.transfer.ScreenValue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("serial")
public class MonthlyAccTableModel extends PropertyTableModel
{
	private List<MonthlyAccount> _accList;
	
	public static final String[] COLUMN_TITLES = { "Account Name" };
	public static final Class<?>[] COLUMN_CLASSES = {  ScreenValue.class };

	@Override
	public Class<?> getColumnClass(int col)
	{
		return COLUMN_CLASSES[col];
	}

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
		_accList.add(new MonthlyAccount(_accList.size() + 1, ""));
		fireTableDataChanged();
	}

	@Override
	public void removeRow(int selectedRow)
	{
		if(selectedRow >= 0 && selectedRow < _accList.size())
		{
			int seq = _accList.get(selectedRow).sequenceNo;
			_accList.remove(selectedRow);
			
			for (int i = selectedRow; i < _accList.size(); i++)
			{
				MonthlyAccount current = _accList.get(i);
				_accList.set(i, new MonthlyAccount(seq, current.getAccName()));
				seq = current.sequenceNo;
			}
			
			fireTableDataChanged();
		}
	}

	@Override
	public boolean isValid()
	{
		for (MonthlyAccount acc : _accList)
		{
			if(!acc.isValid()) return false;
		}
		return true;
	}

	public void moveUp(int selectedRow)
	{
		swapRows(selectedRow - 1, selectedRow);
	}

	public void moveDown(int selectedRow)
	{
		swapRows(selectedRow, selectedRow + 1);
	}

	private void swapRows(int first, int second)
	{
		if(first >= 0 && first < _accList.size() && second < _accList.size())
		{
			MonthlyAccount firstAcc = _accList.get(first);
			MonthlyAccount secondAcc = _accList.get(second);
			
			_accList.set(second, new MonthlyAccount(secondAcc.sequenceNo, firstAcc.getAccName()));
			_accList.set(first, new MonthlyAccount(firstAcc.sequenceNo, secondAcc.getAccName()));
			
			fireTableDataChanged();
		}
	}

}
