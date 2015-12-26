package gncimport.tests.unit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import gncimport.models.AccountData;
import gncimport.tests.data.SampleTxData;
import gncimport.ui.swing.TxTableModel;

import org.junit.Before;
import org.junit.Test;

public class TxTableModelTests
{
	private TxTableModel _model;

	@Before
	public void setUp()
	{
		_model = new TxTableModel(SampleTxData.txDataListWithSomeAccounts());
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
		assertThat(_model.getColumnName(3), is("Account"));
		assertThat(_model.getColumnName(4), is("Ignore"));
	}

	@Test
	public void returns_proper_column_count()
	{
		assertThat(_model.getColumnCount(), is(5));
	}

	@Test
	public void maps_transaction_fields_to_table_columns()
	{
		assertThat((String) (_model.getValueAt(0, 0)), is("11/15/2012"));
		assertThat((String) (_model.getValueAt(1, 1)), is("98.00"));
		assertThat((String) (_model.getValueAt(1, 2)), is("Groceries"));
		assertThat((String) (_model.getValueAt(0, 3)), is("Expenses"));
	}

	@Test
	public void can_handle_rows_without_account_set()
	{
		assertThat((String) (_model.getValueAt(1, 3)), is(""));
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

	@Test
	public void allows_editing_target_account_column()
	{
		AccountData newAccount = new AccountData("New Acc", "id");

		_model.setValueAt(newAccount, 0, 3);

		assertThat((String) (_model.getValueAt(0, 3)), is("New Acc"));
	}

	@Test
	public void allows_editing_description_column()
	{
		_model.setValueAt("Groceries - Metro Market", 1, 2);

		assertThat((String) (_model.getValueAt(1, 2)), is("Groceries - Metro Market"));
	}

	@Test
	public void default_value_for_ignore_flag_is_false()
	{
		assertThat((Boolean) false, is((Boolean) _model.getValueAt(0, 4)));
	}

	@Test
	public void allows_changing_the_ignore_flag()
	{
		_model.setValueAt(new Boolean(true), 1, 4);

		assertThat((Boolean) (_model.getValueAt(1, 4)), is(new Boolean(true)));
	}
}
