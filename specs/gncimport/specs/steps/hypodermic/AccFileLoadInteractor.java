package gncimport.specs.steps.hypodermic;

import gncimport.models.TxImportModel;

public class AccFileLoadInteractor
{
	private TxImportModel _model;

	public AccFileLoadInteractor(TxImportModel model)
	{
		_model = model;
	}


	public void openGncFile(String fileName)
	{
		_model.openGncFile(fileName);		
	}

}
