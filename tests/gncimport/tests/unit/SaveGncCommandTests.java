package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.interactors.TxImportInteractor;
import gncimport.models.TxData;
import gncimport.tests.data.SampleTxData;
import gncimport.ui.TxView;
import gncimport.ui.commands.SaveGncCommand;
import gncimport.ui.events.SaveGncEvent;
import gncimport.ui.swing.TxTableModel;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SaveGncCommandTests
{
	private TxView _view;
	private TxImportInteractor _interactor;
	private SaveGncCommand _cmd;

	@Before
	public void Setup()
	{
		_view = mock(TxView.class);
		_interactor = mock(TxImportInteractor.class);
		_cmd = new SaveGncCommand(_view, _interactor);
	}

	@Test
	public void forwards_transactions_from_view_to_interactor()
	{
		List<TxData> expectedTxs = SampleTxData.txDataList();

		when(_view.getTxTableModel()).thenReturn(new TxTableModel(expectedTxs));

		_cmd.execute(new SaveGncEvent("/path/to/file.gnucash"));

		verify(_interactor).saveTxTo(expectedTxs, "/path/to/file.gnucash");
	}
}
