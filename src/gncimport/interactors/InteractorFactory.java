package gncimport.interactors;

import gncimport.models.RuleModel;
import gncimport.models.TxImportModel;

public class InteractorFactory
{
	private TxImportModel _txImportModel;
	private RuleModel _propertyModel;
	
	public InteractorFactory(TxImportModel importModel, RuleModel propModel)
	{
		_txImportModel = importModel;
		_propertyModel = propModel;
	}
	
	public TxBrowseInteractor txBrowse(TxBrowseInteractor.OutPort boundary)
	{
		return new TxBrowseInteractor(boundary, _txImportModel);		
	}

	public AccFileLoadInteractor accFileLoad(AccFileLoadInteractor.OutPort boundary)
	{
		return new AccFileLoadInteractor(boundary, _txImportModel);
	}

	public AccSelectionInteractor accSelection(AccSelectionInteractor.OutPort boundary)
	{
		return new AccSelectionInteractor(boundary, _txImportModel);
	}

	public TxImportInteractor txImport()
	{
		return new TxImportInteractor(_txImportModel);
	}

	public PropertyEditInteractor propertyEdit(PropertyEditInteractor.OutPort boundary)
	{
		return new PropertyEditInteractor(boundary, _propertyModel);
	}
}