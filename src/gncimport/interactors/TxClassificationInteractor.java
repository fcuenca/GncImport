package gncimport.interactors;

import gncimport.models.AccountData;
import gncimport.models.TxImportModel;

import java.util.List;

public class TxClassificationInteractor
{
	public interface OutPort
	{
		void accept(List<AccountData> accounts);
	}

	private OutPort _output;
	private TxImportModel _model;

	public TxClassificationInteractor(OutPort output, TxImportModel model)
	{
		_output = output;
		_model = model;
	}

	public void getCandidateTargetAccounts()
	{
		List<AccountData> accounts = _model.getCandidateTargetAccounts();
		_output.accept(accounts);	
	}

}
