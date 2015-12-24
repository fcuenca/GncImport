package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.interactors.AccFileLoadInteractor;
import gncimport.models.TxImportModel;

import org.junit.Before;
import org.junit.Test;

public class AccFileLoadInteractorTests
{

	private AccFileLoadInteractor.OutPort _outPort;
	private TxImportModel _model;
	private AccFileLoadInteractor _interactor;
	
	
	@Before
	public void Setup()
	{
		_outPort = mock(AccFileLoadInteractor.OutPort.class);
		_model = mock(TxImportModel.class);
		
		_interactor = new AccFileLoadInteractor(_outPort, _model);
	}

	@Test
	public void opens_accounting_file_and_notifies_the_view()
	{
		_interactor.openGncFile("/path/to/file.gnc");
		
		verify(_model).openGncFile("/path/to/file.gnc");
		verify(_outPort).fileWasOpened("/path/to/file.gnc");
	}

}
