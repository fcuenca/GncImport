package gncimport.interactors;

import gncimport.models.TxImportModel;

public class InteractorFactory
{
	private TxImportModel _model;
	
	public InteractorFactory(TxImportModel model)
	{
		_model = model;
	}
	
	public TxFileLoadInteractor txFileLoad(TxFileLoadInteractor.OutPort boundary)
	{
		return new TxFileLoadInteractor(boundary, _model);		
	}

	public AccFileLoadInteractor accFileLoad()
	{
		return new AccFileLoadInteractor(_model);
	}

	public AccSelectionInteractor accSelection(AccSelectionInteractor.OutPort boundary)
	{
		return new AccSelectionInteractor(boundary, _model);
	}

	public TxClassificationInteractor txClassification(TxClassificationInteractor.OutPort boundary)
	{
		return new TxClassificationInteractor(boundary, _model);
	}

	public TxImportInteractor txImport()
	{
		return new TxImportInteractor(_model);
	}
}