package gncimport.specs.steps.hypodermic;

import gncimport.models.TxImportModel;

public class AccFileLoadingInteractor
{
	private TxImportModel _model;
	//private AccFileLoadingOutputBoundary _output;

	public AccFileLoadingInteractor(TxImportModel model)
	{
		_model = model;
		//_output = output;
	}


	public void openGncFile(String fileName)
	{
		_model.openGncFile(fileName);		
	}

}
