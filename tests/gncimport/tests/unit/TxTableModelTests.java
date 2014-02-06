package gncimport.tests.unit;

import static gncimport.tests.unit.ListUtils.list_of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import gncimport.models.TxData;
import gncimport.ui.TxTableModel;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TxTableModelTests
{
	private List<TxData> _actualTxs;
	private TxTableModel _model;

	@Before
	public void setUp()
	{
		_actualTxs = list_of(
				new TxData("Nov 15, 2012", 12, "Taxi ride"),
				new TxData("Dec 17, 2012", 98, "Groceries"));

		_model = new TxTableModel(_actualTxs);
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
		assertThat((Double) (_model.getValueAt(1, 1)), is((Double) 98.0));
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
