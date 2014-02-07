package gncimport.tests.unit;

import static gncimport.tests.unit.ListUtils.list_of;
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
		List<TxData> actualTxs = list_of(
				new TxData("Nov 15, 2012", 12, "Taxi ride"),
				new TxData("Dec 17, 2012", 98, "Groceries"));

		TxTableModel txTableModel = new TxTableModel(actualTxs);

		when(_view.getTxTableModel()).thenReturn(txTableModel);

		_presenter.onSaveToGncFile("/path/to/file.gnucash");

		verify(_model).saveTxTo(actualTxs, "/path/to/file.gnucash");
	}

}
