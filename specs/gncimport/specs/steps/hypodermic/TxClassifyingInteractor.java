package gncimport.specs.steps.hypodermic;

import gncimport.models.AccountData;
import gncimport.models.TxImportModel;

import java.util.List;

public class TxClassifyingInteractor
{
	private TxClassifyingOutputBoundary _output;
	private TxImportModel _model;

	public TxClassifyingInteractor(TxClassifyingOutputBoundary output, TxImportModel model)
	{
		_output = output;
		_model = model;
	}

	public void getCandidateTargetAccounts()
	{
		List<AccountData> accounts = _model.getCandidateTargetAccounts();
		_output.setResponse(accounts);	
	}

}
