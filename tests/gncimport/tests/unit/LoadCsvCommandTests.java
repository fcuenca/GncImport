package gncimport.tests.unit;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.ui.LoadCsvCommand;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;

import org.junit.Before;
import org.junit.Test;

public class LoadCsvCommandTests
{
	private TxView _view;
	private UIConfig _config;
	private TxBrowseInteractor _interactor;
	private LoadCsvCommand _cmd;

	@Before
	public void SetUp()
	{
		_view = mock(TxView.class);
		_config = mock(UIConfig.class);
		_interactor = mock(TxBrowseInteractor.class);
		
		_cmd = new LoadCsvCommand(_view, _config, _interactor);
	}

	@Test
	public void prompts_for_opening_csv_file_at_the_last_known_location()
	{
		when(_config.getLastCsvDirectory()).thenReturn("/path/to/input");

		_cmd.execute(null);

		verify(_view).promptForFile("/path/to/input");
	}
	
	@Test
	public void defaults_to_homeDir_if_there_is_no_last_known_location()
	{
		String homeDir = System.getProperty("user.home");

		when(_config.getLastCsvDirectory()).thenReturn("");

		_cmd.execute(null);

		verify(_view).promptForFile(homeDir);
	}
	
	@Test
	public void fetches_transactions_from_file()
	{
		when(_view.promptForFile(anyString())).thenReturn("/path/to/input/file.csv");

		_cmd.execute(null);
		
		verify(_interactor).fetchTransactions("/path/to/input/file.csv");
	}

	@Test
	public void can_handle_cancel_open_file_operation()
	{
		when(_view.promptForFile(anyString())).thenReturn(null);

		_cmd.execute(null);

		verify(_interactor, never()).fetchTransactions(anyString());
		verify(_config, never()).setLastCsvDirectory(anyString());
		verify(_view, never()).updateCsvFileLabel(anyString());
	}


}
