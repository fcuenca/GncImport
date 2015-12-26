package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.interactors.AccFileLoadInteractor;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;
import gncimport.ui.commands.LoadFileCommand;
import gncimport.ui.commands.LoadGncCommand;

import org.junit.Before;
import org.junit.Test;

public class LoadGncCommandTests extends LoadFileCommandContractTests
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

	@Override
	protected LoadFileCommand newCommandToOpenFileFromLocation(String filePath, TxView view)
	{
		when(_config.getLastGncDirectory()).thenReturn(filePath);

		return new LoadGncCommand(view, _config, _interactor);
	}
	
	@Test
	public void opens_accounting_file()
	{
		_cmd.loadFile("/path/to/input/file.gnc");
		
		verify(_interactor).openGncFile("/path/to/input/file.gnc");
	}
	
	@Test
	public void returns_last_used_directory()
	{
		when(_config.getLastGncDirectory()).thenReturn("/some/file/path");
		
		assertThat(_cmd.getLastUsedDirectory(), is("/some/file/path"));
	}


}
