package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.ui.Command;
import gncimport.ui.CreateAccHierarchyCommand;
import gncimport.ui.CreateAccHierarchyEvent;
import gncimport.ui.Event;
import gncimport.ui.FilterTxListCommand;
import gncimport.ui.FilterTxListEvent;
import gncimport.ui.LoadCsvCommand;
import gncimport.ui.LoadGncCommand;
import gncimport.ui.NoArgsEvent;
import gncimport.ui.SaveGncCommand;
import gncimport.ui.SaveGncEvent;
import gncimport.ui.SelectExpenseAccCommand;
import gncimport.ui.SelectExpenseAccEvent;
import gncimport.ui.SelectSourceAccCommand;
import gncimport.ui.SelectTargetAccCommand;
import gncimport.ui.TxView;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class CommandExceptionHandlingContractTests<T extends Event>
{
	private static TxView _view = mock(TxView.class);
	private static RuntimeException _expectedException = new RuntimeException("ahhhhh: exception wasn't handled!");

	private Command<T> _cmd;
	
    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
    	return Arrays.asList(
				new Object[][]
				{
					{
						new CreateAccHierarchyCommand(_view, null, null) {
							@Override
							protected void doExecute(CreateAccHierarchyEvent args)
							{
								throw _expectedException;
							}
						}
					},
					{
						new FilterTxListCommand(_view, null) {
							@Override
							protected void doExecute(FilterTxListEvent event)
							{
								throw _expectedException;
							}							
						}
					},
					{
						new SelectExpenseAccCommand(_view, null) {
							@Override
							protected void doExecute(SelectExpenseAccEvent args)
							{
								throw _expectedException;
							}
						}
					},
					{
						new LoadCsvCommand(_view, null, null) {
							@Override
							protected void doExecute(NoArgsEvent __not_used__)
							{
								throw _expectedException;
							}
						}
					},
					{
						new LoadGncCommand(_view, null, null) {
							@Override
							protected void doExecute(NoArgsEvent __not_used__)
							{
								throw _expectedException;
							}
						}
					},
					{
						new SaveGncCommand(_view, null) {
							@Override
							protected void doExecute(SaveGncEvent event)
							{
								throw _expectedException;
							}
						}
					},
					{
						new SelectSourceAccCommand(_view, null) {
							@Override
							protected void doExecute(NoArgsEvent __not_used__)
							{
								throw _expectedException;
							}
						}
					},
					{
						new SelectTargetAccCommand(_view, null) {
							@Override
							protected void doExecute(NoArgsEvent __not_used__)
							{
								throw _expectedException;
							}
						}
					}
				}
				);
    }

    public CommandExceptionHandlingContractTests(Command<T> cmd)
    {
    	_cmd = cmd;
    }
    
	@Before
	public void setUp()
	{
		// this mock is static, so it's reused from test to test
		// therefore, it needs to be reset each time
		Mockito.reset(_view); 
	}

	@Test
	public void notifies_view_when_command_blows_up()
	{
		_cmd.execute(null);
		
		verify(_view).handleException(_expectedException);
	}
}
