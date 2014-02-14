package gncimport.tests.unit;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.boundaries.TxModel;
import gncimport.boundaries.TxView;
import gncimport.models.TxData;
import gncimport.ui.MainWindowPresenter;
import gncimport.ui.TxTableModel;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PresenterSavesImportedTransactiosToGncFile
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
	public void transactions_from_view_are_forwarded_to_model()
	{
		List<TxData> actualTxs = SampleTxData.txDataList();

		TxTableModel txTableModel = new TxTableModel(actualTxs);

		when(_view.getTxTableModel()).thenReturn(txTableModel);
		when(_view.getSourceAccountId()).thenReturn("acc-id");

		_presenter.onSaveToGncFile("/path/to/file.gnucash");

		verify(_model).saveTxTo(actualTxs, "acc-id", "/path/to/file.gnucash");
	}

	@Test
	public void notifies_view_on_exceptions()
	{
		RuntimeException exception = new RuntimeException();

		when(_view.getTxTableModel()).thenReturn(new TxTableModel(SampleTxData.txDataList()));

		doThrow(exception).when(_model).saveTxTo(anyListOf(TxData.class), anyString(), anyString());

		_presenter.onSaveToGncFile("/path/to/file.gnucash");

		verify(_view).handleException(exception);
	}
}
