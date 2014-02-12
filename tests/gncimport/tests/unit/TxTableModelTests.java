package gncimport.tests.unit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import gncimport.ui.TxTableModel;

import org.junit.Before;
import org.junit.Test;

public class TxTableModelTests
{
	private TxTableModel _model;

	@Before
	public void setUp()
	{
		_model = new TxTableModel(SampleTxData.txDataList());
	}

	@Test
	public void can_report_transaction_count()
	{
		assertThat(_model.getRowCount(), is(2));
	}

	@Test
	public void can_report_column_names()
	{
		assertThat(_model.getColumnName(0), is("Date"));
		assertThat(_model.getColumnName(1), is("Amount"));
		assertThat(_model.getColumnName(2), is("Description"));
	}

	@Test
	public void returns_proper_column_count()
	{
		assertThat(_model.getColumnCount(), is(3));
	}

	@Test
	public void maps_transaction_fields_to_table_columns()
	{
		assertThat((String) (_model.getValueAt(0, 0)), is("Nov 15, 2012"));
		assertThat((String) (_model.getValueAt(1, 1)), is("98.00"));
		assertThat((String) (_model.getValueAt(1, 2)), is("Groceries"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void verifies_column_bounds()
	{
		_model.getValueAt(0, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void verifies_row_bounds()
	{
		_model.getValueAt(2, 0);
	}
}
