package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.transfer.MonthlyAccountParam;
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
		List<MonthlyAccountParam> accList = new ArrayList<MonthlyAccountParam>(ListUtils.list_of(
				new MonthlyAccountParam(1, "Misc Expenses"),
				new MonthlyAccountParam(2, "Groceries"),
				new MonthlyAccountParam(3, "Living Expenses")));
		
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
		assertThat(_tableModel.getValueAt(0, 1), is((Object)"Misc Expenses"));
		
		assertThat(_tableModel.getValueAt(1, 0), is((Object)2));
		assertThat(_tableModel.getValueAt(1, 1), is((Object)"Groceries"));
		
		assertThat(_tableModel.getValueAt(2, 0), is((Object)3));
		assertThat(_tableModel.getValueAt(2, 1), is((Object)"Living Expenses"));
	}


	@Test(expected=IllegalArgumentException.class)
	public void account_list_cannot_be_null()
	{
		new MonthlyAccTableModel(null);	
	}


}
