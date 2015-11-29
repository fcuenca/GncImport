package gncimport.specs.steps.hypodermic;

import gncimport.models.AccountData;
import gncimport.models.TxImportModel;

import java.util.List;

public class AccSelectionInteractor
{
	public interface OutPort
	{
		void accept(List<AccountData> accounts);
	}

	private TxImportModel _model;
	private OutPort _output;

	public AccSelectionInteractor(OutPort output, TxImportModel model)
	{
		_model = model;
		_output = output;
	}

	public void selectAccount()
	{
		List<AccountData> accounts = _model.getAccounts();
		_output.accept(accounts);
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
