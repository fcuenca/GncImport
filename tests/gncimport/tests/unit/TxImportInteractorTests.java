package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.interactors.TxImportInteractor;
import gncimport.models.TxImportModel;
import gncimport.tests.data.SampleTxData;
import gncimport.transfer.TxData;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TxImportInteractorTests
{
	private TxImportModel _model;
	private TxImportInteractor _interactor;
	
	@Before
	public void Setup()
	{
		_model = mock(TxImportModel.class);
		_interactor = new TxImportInteractor(_model);
	}

	@Test
	public void forwards_save_request_to_model()
	{
		List<TxData> expectedTxs = SampleTxData.txDataList();

		_interactor.saveTxTo(expectedTxs, "/path/to/file.gnc");
		
		verify(_model).saveTxTo(expectedTxs, "/path/to/file.gnc");
	}

}
