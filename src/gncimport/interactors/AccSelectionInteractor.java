package gncimport.interactors;

import gncimport.models.AccountData;
import gncimport.models.TxImportModel;

import java.util.List;

public class AccSelectionInteractor
{
	public interface OutPort
	{
		AccountData accept2(List<AccountData> accounts); //TODO
		void accept(List<AccountData> accounts);
		void targetHierarchyHasBeenSet(String accName, List<AccountData> candidateAccList);
		void sourceAccHasBenSet(String accName);
	}

	private TxImportModel _model;
	private OutPort _output;

	public AccSelectionInteractor(OutPort output, TxImportModel model)
	{
		_model = model;
		_output = output;
	}

	public void browseAccounts()
	{
		List<AccountData> accounts = _model.getAccounts();
		_output.accept(accounts);
	}

	public void setTargetHierarchy(AccountData acc)
	{
		_model.setTargetHierarchy(acc);	
		_output.targetHierarchyHasBeenSet(acc.getName(), _model.getCandidateTargetAccounts());
	}

	public void setSourceAccount(AccountData acc)
	{
		_model.setSourceAccount(acc);	
		_output.sourceAccHasBenSet(acc.getName());
	}

	public void selectSourceAccount()
	{
		List<AccountData> accounts = _model.getAccounts();
				
		AccountData selectedAccount = _output.accept2(accounts);

		if (selectedAccount != null)
		{
			setSourceAccount(selectedAccount);
		}
	}

	public void selectTargetAccount()
	{
		List<AccountData> accounts = _model.getAccounts();
		
		AccountData selectedAccount = _output.accept2(accounts);

		if (selectedAccount != null)
		{
			setTargetHierarchy(selectedAccount);
		}
		
	}

}
