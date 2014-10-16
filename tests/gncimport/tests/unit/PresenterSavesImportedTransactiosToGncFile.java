package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.models.TxData;
import gncimport.models.TxImportModel;
import gncimport.tests.data.SampleTxData;
import gncimport.ui.MainWindowPresenter;
import gncimport.ui.TxTableModel;
import gncimport.ui.TxView;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PresenterSavesImportedTransactiosToGncFile
{

	private MainWindowPresenter _presenter;
	private TxView _view;
	private TxImportModel _model;

	@Before
	public void SetUp()
	{
		_model = mock(TxImportModel.class);
		_view = mock(TxView.class);
		_presenter = new MainWindowPresenter(_model, _view);
	}

	@Test
	public void transactions_from_view_are_forwarded_to_model()
	{
		List<TxData> actualTxs = SampleTxData.txDataList();

		TxTableModel txTableModel = new TxTableModel(actualTxs);

		when(_view.getTxTableModel()).thenReturn(txTableModel);

		_presenter.onSaveToGncFile("/path/to/file.gnucash");

		verify(_model).saveTxTo(actualTxs, "/path/to/file.gnucash");
	}

}
