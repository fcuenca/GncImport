package gncimport.interactors;

import gncimport.models.AccountData;
import gncimport.models.Month;
import gncimport.models.MonthlyAccountParam;
import gncimport.models.TxImportModel;

import java.util.List;

public class AccHierarchyCreationInteractor
{

	private TxImportModel _model;

	public AccHierarchyCreationInteractor(TxImportModel model)
	{
		_model = model;
	}

	public void createNewAccountHierarchy(
			AccountData parentAcc, String hierarchyRoot, Month month,
			List<MonthlyAccountParam> subAccList, String fileNameToSave)
	{
		_model.createNewAccountHierarchy(
				parentAcc, hierarchyRoot, month, subAccList, fileNameToSave);		
	}

}
