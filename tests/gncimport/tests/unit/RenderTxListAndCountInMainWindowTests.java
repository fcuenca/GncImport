package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.presenters.MainWindowPresenter;
import gncimporter.boundaries.TxModel;
import gncimporter.boundaries.TxView;

import org.junit.Before;
import org.junit.Test;

public class RenderTxListAndCountInMainWindowTests
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
	public void displays_transaction_count_on_init()
	{
		when(_model.getTxCount()).thenReturn(20);

		_presenter.onInit();

		verify(_view).displayTxCount(20);
	}

}
