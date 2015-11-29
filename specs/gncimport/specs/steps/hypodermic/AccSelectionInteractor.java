package gncimport.specs.steps.hypodermic;

import gncimport.models.AccountData;
import gncimport.models.TxImportModel;

import java.util.List;

public class AccSelectionInteractor
{
	private TxImportModel _model;
	private AccSelectionOutputBoundary _output;

	public AccSelectionInteractor(AccSelectionOutputBoundary output, TxImportModel model)
	{
		_model = model;
		_output = output;
	}

	public void selectAccount()
	{
		List<AccountData> accounts = _model.getAccounts();
		_output.setResponse(accounts);
	}

	public void setTargetHierarchy(AccountData acc)
	{
		_model.setTargetHierarchy(acc);							
	}

	public void setSourceAccount(AccountData acc)
	{
		_model.setSourceAccount(acc);									
	}

}
