package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.fail;
import gncimport.interactors.AccFileLoadInteractor;
import gncimport.ui.LoadGncCommand;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;

import org.junit.Before;
import org.junit.Test;

public class LoadGncCommandTests
{
	
	private TxView _view;
	private UIConfig _config;
	private AccFileLoadInteractor _interactor;
	private LoadGncCommand _cmd;

	@Before
	public void Setup()
	{
		_view = mock(TxView.class);
		_config = mock(UIConfig.class);
		_interactor = mock(AccFileLoadInteractor.class);
		
		_cmd = new LoadGncCommand(_view, _config, _interactor);
	}

	@Test
	public void prompts_for_opening_gnc_file_at_the_last_known_location()
	{
		when(_config.getLastGncDirectory()).thenReturn("/path/to/input");

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
}
