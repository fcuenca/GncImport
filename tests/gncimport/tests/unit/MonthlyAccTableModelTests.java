package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.transfer.MonthlyAccount;
import gncimport.transfer.ScreenValue;
import gncimport.ui.swing.MonthlyAccTableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.junit.Before;
import org.junit.Test;

public class MonthlyAccTableModelTests
{
	private final int IRRELEVANT = new Random().nextInt();

	private MonthlyAccTableModel _tableModel;
	private List<MonthlyAccount> _accList;
	
	@Before
	public void setUp() throws Exception
	{
		_accList = new ArrayList<MonthlyAccount>(ListUtils.list_of(
				new MonthlyAccount(2, "Groceries"),
				new MonthlyAccount(1, "Misc Expenses"),
				new MonthlyAccount(3, "Living Expenses")));
		
		_tableModel = new MonthlyAccTableModel(_accList);
	}

	@Test
	public void all_columns_have_title()
	{
		assertThat(_tableModel.getColumnCount(), is(MonthlyAccTableModel.COLUMN_TITLES.length));
	}
	
	@Test
	public void account_name_column_is_editable()
	{		
		assertThat(_tableModel.isCellEditable(IRRELEVANT, IRRELEVANT), is(true));		
	}
	
	@Test
	public void displays_account_names_in_order()
	{
		assertThat(_tableModel.getRowCount(), is(3));
		assertThat(_tableModel.getValueAt(0, IRRELEVANT), is((Object)(new ScreenValueForTest("Misc Expenses"))));
		assertThat(_tableModel.getValueAt(1, IRRELEVANT), is((Object)(new ScreenValueForTest("Groceries"))));
		assertThat(_tableModel.getValueAt(2, IRRELEVANT), is((Object)(new ScreenValueForTest("Living Expenses"))));
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
		}
	}

	@Test
	public void updates_rule_definitions()
	{		
		_tableModel.setValueAt("some other account", 0, IRRELEVANT);
		
		ScreenValue newValue = (ScreenValue) _tableModel.getValueAt(0, IRRELEVANT);
		
		assertThat(newValue, is((ScreenValue)new ScreenValueForTest("some other account")));
		assertThat(newValue.domainValue(), is((Object)new MonthlyAccount(1, "some other account")));
	}
	
	@Test
	public void valid_when_all_rules_are_valid()
	{		
		assertThat(_tableModel.isValid(), is(true));
	}

	@Test
	public void invalid_when_at_least_one_rule_is_invalid()
	{
		_accList.add(new MonthlyAccount(IRRELEVANT, "irrelevant") { @Override public Boolean isValid() { return false; } } );
				
		assertThat(_tableModel.isValid(), is(false));
	}
	
	@Test
	public void rules_can_be_appended()
	{		
		_tableModel.newRow();
		
		assertThat(_tableModel.getRowCount(), is(4));
		
		ScreenValue newValue = (ScreenValue) _tableModel.getValueAt(3, IRRELEVANT);
		assertThat(newValue, is((ScreenValue)new ScreenValueForTest("", false)));
		assertThat(newValue.domainValue(), is((Object)new MonthlyAccount(4, "")));

	}
	
	@Test
	public void adding_account_notifies_listeners()
	{
		TableModelListener listener = mock(TableModelListener.class);
		_tableModel.addTableModelListener(listener);
		
		_tableModel.newRow();
		
		verify(listener).tableChanged(any(TableModelEvent.class));
	}

	@Test
	public void rules_can_be_removed()
	{
		_tableModel.removeRow(1);		
		assertThat(_tableModel.getRowCount(), is(2));
	}

	@Test
	public void removing_rules_out_of_bounds_is_ignored()
	{
		_tableModel.removeRow(-1);
		_tableModel.removeRow(3);
		
		assertThat(_tableModel.getRowCount(), is(3));
	}

	@Test
	public void removing_rule_notifies_listeners()
	{
		TableModelListener listener = mock(TableModelListener.class);
		_tableModel.addTableModelListener(listener);
		
		_tableModel.removeRow(0);
		
		verify(listener).tableChanged(any(TableModelEvent.class));
	}
	
	@Test
	public void accounts_are_renumbered_after_removal()
	{
		_tableModel.removeRow(1);
		
		assertThat(_accList.get(0).sequenceNo, is(1));
		assertThat(_accList.get(1).sequenceNo, is(2));
	}
	
	@Test
	public void accounts_can_be_moved_up()
	{
		_tableModel.moveUp(1);
		
		assertThat(_tableModel.getValueAt(0, IRRELEVANT), is((Object)(new ScreenValueForTest("Groceries"))));
		assertThat(_tableModel.getValueAt(1, IRRELEVANT), is((Object)(new ScreenValueForTest("Misc Expenses"))));
		assertThat(_tableModel.getValueAt(2, IRRELEVANT), is((Object)(new ScreenValueForTest("Living Expenses"))));
	}

	@Test
	public void accounts_are_renumbered_after_moving_up()
	{
		_tableModel.moveUp(1);
		
		assertThat(_accList.get(0).sequenceNo, is(1));
		assertThat(_accList.get(1).sequenceNo, is(2));
	}
}
