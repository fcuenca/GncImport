package gncimport.tests.unit;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.ui.LoadFileCommand;
import gncimport.ui.TxView;

import org.junit.Test;

public abstract class LoadFileCommandContractTests
{
	@Test
	public void prompts_for_opening_file_at_the_last_known_location()
	{
		TxView view = mock(TxView.class);
		LoadFileCommand cmd = newCommandToOpenFileFromLocation("/path/to/input", view); 
		
		cmd.execute(null);

		verify(view).promptForFile("/path/to/input");
	}

	@Test
	public void defaults_to_homeDir_if_there_is_no_last_known_location()
	{
		String homeDir = System.getProperty("user.home");
		TxView view = mock(TxView.class);
		LoadFileCommand cmd = newCommandToOpenFileFromLocation("", view); 

		cmd.execute(null);

		verify(view).promptForFile(homeDir);
	}
	
	@Test
	public void can_handle_cancel_open_file_operation()
	{
		TxView view = mock(TxView.class);
		LoadFileCommand cmd = spy(newCommandToOpenFileFromLocation("/path/to/file", view)); 

		when(view.promptForFile(anyString())).thenReturn(null);

		cmd.execute(null);
		
		verify(cmd, never()).loadFile(anyString());
		verify(view, never()).updateCsvFileLabel(anyString());
	}
	
	@Test
	public void opens_file_when_selection_is_made()
	{
		TxView view = mock(TxView.class);
		LoadFileCommand cmd = spy(newCommandToOpenFileFromLocation("/path/to/input/file.ext", view)); 

		when(view.promptForFile(anyString())).thenReturn("/path/to/input/file.ext");

		cmd.execute(null);
		
		verify(cmd).loadFile("/path/to/input/file.ext");
	}

	protected abstract LoadFileCommand newCommandToOpenFileFromLocation(String filePath, TxView view);
}
