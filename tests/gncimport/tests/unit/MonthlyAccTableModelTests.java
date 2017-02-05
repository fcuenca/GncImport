package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.transfer.MonthlyAccount;
import gncimport.ui.swing.MonthlyAccTableModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MonthlyAccTableModelTests
{

	private MonthlyAccTableModel _tableModel;
	
	@Before
	public void setUp() throws Exception
	{
		List<MonthlyAccount> accList = new ArrayList<MonthlyAccount>(ListUtils.list_of(
				new MonthlyAccount(2, "Groceries"),
				new MonthlyAccount(1, "Misc Expenses"),
				new MonthlyAccount(3, "Living Expenses")));
		
		_tableModel = new MonthlyAccTableModel(accList);
	}

	@Test
	public void all_columns_have_title()
	{
		assertThat(_tableModel.getColumnCount(), is(MonthlyAccTableModel.COLUMN_TITLES.length));
	}
	
	@Test
	public void only_account_name_column_is_editable()
	{		
		assertThat(_tableModel.isCellEditable(1, 0), is(false));			
		assertThat(_tableModel.isCellEditable(1, 1), is(true));		
	}
	
	@Test
	public void displays_account_names_in_order()
	{
		assertThat(_tableModel.getRowCount(), is(3));
		
		assertThat(_tableModel.getValueAt(0, 0), is((Object)1));
		assertThat(_tableModel.getValueAt(0, 1), is((Object)(new ScreenValueForTest("Misc Expenses"))));
		
		assertThat(_tableModel.getValueAt(1, 0), is((Object)2));
		assertThat(_tableModel.getValueAt(1, 1), is((Object)(new ScreenValueForTest("Groceries"))));
		
		assertThat(_tableModel.getValueAt(2, 0), is((Object)3));
		assertThat(_tableModel.getValueAt(2, 1), is((Object)(new ScreenValueForTest("Living Expenses"))));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void account_list_cannot_be_null()
	{
		new MonthlyAccTableModel(null);	
	}

	@Test
	public void all_columns_have_types_defined()
	{
		assertThat(_tableModel.getColumnCount(), is(MonthlyAccTableModel.COLUMN_CLASSES.length));
	}
	
	@Test
	public void declared_column_types_match_values_returned()
	{
		for(int i = 0; i < MonthlyAccTableModel.COLUMN_CLASSES.length; i++)
		{
			Class<?> actual = _tableModel.getValueAt(0, i).getClass();
			Class<?> declared = _tableModel.getColumnClass(i);
			
			assertThat(String.format("declared class for column %d (%s) is not compatible actual class (%s)", i, declared, actual),
					declared.isAssignableFrom(actual), is(true));
			
			if(i != 0) assertThat("declared class for column " + i + " can not be Object",
						declared.equals(Object.class), is(false));
		}
	}



}
