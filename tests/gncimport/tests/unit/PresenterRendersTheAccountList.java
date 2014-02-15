package gncimport.tests.unit;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.boundaries.TxModel;
import gncimport.boundaries.TxView;
import gncimport.ui.MainWindowPresenter;

import java.util.HashMap;
import java.util.Map;

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
	public void renders_accounts_on_file_open()
	{
		Map<String, String> accountList = new HashMap<String, String>();

		when(_model.getAccounts()).thenReturn(accountList);
		when(_model.getDefaultSourceAccountId()).thenReturn("acc-id");

		_presenter.onLoadGncFile("/path/to/gnc.xml");

		verify(_model).openGncFile("/path/to/gnc.xml");
		verify(_view).displayAccounts(accountList, "acc-id");
	}

	@Test
	public void notifies_view_on_failure_open_file()
	{
		RuntimeException exception = new RuntimeException();

		doThrow(exception).when(_model).openGncFile("/path/to/gnc.xml");

		_presenter.onLoadGncFile("/path/to/gnc.xml");

		verify(_view).handleException(exception);
	}

	@Test
	public void notifies_view_on_gnc_parsing_failure()
	{
		RuntimeException exception = new RuntimeException();

		doThrow(exception).when(_model).getAccounts();

		_presenter.onLoadGncFile("/path/to/gnc.xml");

		verify(_view).handleException(exception);
	}
}
