package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.ui.LoadCsvCommand;
import gncimport.ui.LoadFileCommand;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;

import org.junit.Before;
import org.junit.Test;

public class LoadCsvCommandTests extends LoadFileCommandContractTests
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
	
	@Override
	protected LoadFileCommand newCommandToOpenFileFromLocation(String filePath, TxView view)
	{
		when(_config.getLastCsvDirectory()).thenReturn(filePath);

		return new LoadCsvCommand(view, _config, _interactor);
	}

	@Test
	public void fetches_transactions_from_file()
	{
		_cmd.loadFile("/path/to/input/file.csv");
		
		verify(_interactor).fetchTransactions("/path/to/input/file.csv");
	}
	
	@Test
	public void returns_last_used_directory()
	{
		when(_config.getLastCsvDirectory()).thenReturn("/some/file/path");
		
		assertThat(_cmd.getLastUsedDirectory(), is("/some/file/path"));
	}
}
