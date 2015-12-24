package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.ui.AccFileLoadPresenter;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;

import org.junit.Before;
import org.junit.Test;

public class AccFileLoadPresenterTests
{
	private AccFileLoadPresenter _presenter;
	private TxView _view;
	private UIConfig _config;

	@Before
	public void Setup()
	{
		_view = mock(TxView.class);
		_config = mock(UIConfig.class);
		
		_presenter = new AccFileLoadPresenter(_view, _config);
	}

	@Test
	public void updates_view_when_transaction_file_is_open()
	{
		_presenter.fileWasOpened("someFileName");
		
		verify(_view).updateGncFileLabel("someFileName");
	}

	@Test
	public void remembers_location_of_last_accounting_file_opened()
	{
		_presenter.fileWasOpened("/path/to/input/file.csv");
		
		verify(_config).setLastGncDirectory("/path/to/input");
	}

}
