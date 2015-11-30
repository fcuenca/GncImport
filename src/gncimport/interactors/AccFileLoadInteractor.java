package gncimport.interactors;

import gncimport.models.TxImportModel;

public class AccFileLoadInteractor
{
	public interface OutPort
	{
		void fileWasOpened(String fileName);
	}
	
	private TxImportModel _model;
	private OutPort _output;

	public AccFileLoadInteractor(OutPort boundary, TxImportModel model)
	{
		_output = boundary;
		_model = model;
	}

	public void openGncFile(String fileName)
	{
		_model.openGncFile(fileName);	
		_output.fileWasOpened(fileName);
	}

}
