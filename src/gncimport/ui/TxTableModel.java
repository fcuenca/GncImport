package gncimport.ui;

import gncimport.models.AccountData;
import gncimport.models.TxData;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TxTableModel extends AbstractTableModel
{
	public static final int DESCRIPTION_COLUMN = 2;
	public static final int ACCOUNT_COLUMN = 3;
	public static final int IGNORE_COLUMN = 4;

	private String[] _columns = new String[] { "Date", "Amount", "Description", "Account", "Ignore" };

	private final List<TxData> _transactions;
	private DecimalFormat _amountFormatter;
	private SimpleDateFormat _dateFormatter;

	public TxTableModel(List<TxData> transactions)
	{
		_amountFormatter = new DecimalFormat();
		_amountFormatter.setMaximumFractionDigits(2);
		_amountFormatter.setMinimumFractionDigits(2);
		_amountFormatter.setGroupingUsed(false);

		_dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

		_transactions = transactions;
	}

	@Override
	public int getColumnCount()
	{
		return _columns.length;
	}

	@Override
	public int getRowCount()
	{
		return _transactions.size();
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		if (row < 0 || row > getRowCount() - 1 || column < 0 || column > getColumnCount() - 1)
		{
			throw new IllegalArgumentException();
		}

		TxData transaction = _transactions.get(row);
		switch (column)
		{
		case 0:
			return _dateFormatter.format(transaction.date);

		case 1:
			return _amountFormatter.format(transaction.amount);

		case 2:
			return transaction.description;

		case 3:
			return transaction.targetAccount != null ?
					transaction.targetAccount.getName() : "";

		case 4:
			return new Boolean(transaction.doNotImport);

		default:
			throw new RuntimeException("this line should be unreachable!");
		}
	}

	@Override
	public String getColumnName(int column)
	{

		return _columns[column];
	}

	@Override
	public String toString()
	{
		return "TxTableModel [" + _transactions + "]";
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return col == ACCOUNT_COLUMN || col == DESCRIPTION_COLUMN || col == IGNORE_COLUMN;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		if (columnIndex == ACCOUNT_COLUMN)
		{
			_transactions.get(rowIndex).targetAccount = (AccountData) aValue;
		}
		else if (columnIndex == IGNORE_COLUMN)
		{
			_transactions.get(rowIndex).doNotImport = ((Boolean) aValue).booleanValue();
		}
		else
		{
			_transactions.get(rowIndex).description = (String) aValue;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if (columnIndex == IGNORE_COLUMN)
		{
			return Boolean.class;
		}
		return super.getColumnClass(columnIndex);
	}

	public List<TxData> getTransactions()
	{
		return _transactions;
	}

}