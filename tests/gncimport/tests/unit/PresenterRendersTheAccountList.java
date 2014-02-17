package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.boundaries.TxModel;
import gncimport.boundaries.TxView;
import gncimport.ui.MainWindowPresenter;

import org.junit.Before;
import org.junit.Test;

public class PresenterRendersTheAccountList
{
	private MainWindowPresenter _presenter;
	private TxView _view;
	private TxModel _model;

	@Before
	public void SetUp()
	{
		_model = mock(TxModel.class);
		_view = mock(TxView.class);
		_presenter = new MainWindowPresenter(_model, _view);
	}

	@Test
	public void renders_default_source_account_on_file_open()
	{
		when(_model.getDefaultSourceAccountName()).thenReturn("RBC Checking");

		_presenter.onLoadGncFile("/path/to/gnc.xml");

		verify(_model).openGncFile("/path/to/gnc.xml");
		verify(_view).displaySourceAccount("RBC Checking");
	}
}
