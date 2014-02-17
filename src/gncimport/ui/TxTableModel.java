package gncimport.ui;

import gncimport.models.TxData;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TxTableModel extends AbstractTableModel
{
	private String[] _columns = new String[] { "Date", "Amount", "Description", "Account" };

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

	public List<TxData> getTransactions()
	{
		return _transactions;
	}
}