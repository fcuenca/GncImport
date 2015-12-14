package gncimport.interactors;

import gncimport.models.TxImportModel;

public class InteractorFactory
{
	private TxImportModel _model;
	
	public InteractorFactory(TxImportModel model)
	{
		_model = model;
	}
	
	public TxBrowseInteractor txBrowse(TxBrowseInteractor.OutPort boundary)
	{
		return new TxBrowseInteractor(boundary, _model);		
	}

	public AccFileLoadInteractor accFileLoad(AccFileLoadInteractor.OutPort boundary)
	{
		return new AccFileLoadInteractor(boundary, _model);
	}

	public AccSelectionInteractor accSelection(AccSelectionInteractor.OutPort boundary)
	{
		return new AccSelectionInteractor(boundary, _model);
	}

	public TxImportInteractor txImport()
	{
		return new TxImportInteractor(_model);
	}
}