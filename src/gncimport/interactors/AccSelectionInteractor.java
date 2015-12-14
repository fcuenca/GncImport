package gncimport.interactors;

import gncimport.models.AccountData;
import gncimport.models.Month;
import gncimport.models.MonthlyAccountParam;
import gncimport.models.TxImportModel;

import java.util.List;

public class AccSelectionInteractor
{
	public interface OutPort
	{
		AccountData accept2(List<AccountData> accounts);
		NewHierarchyOpts accept3(List<AccountData> accounts); 
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

	public AccountData browseAccounts2()
	{
		List<AccountData> accounts = _model.getAccounts();
		return _output.accept2(accounts);
	}
	
	public static class NewHierarchyOpts
	{
		public NewHierarchyOpts(AccountData account, String rootAccName, Month accMonth)
		{
			parentAcc = account;
			hierarchyRoot = rootAccName;
			month = accMonth;
		}
		
		public AccountData parentAcc;
		public String hierarchyRoot;
		public Month month;
		
	}
	
	public void createNewAccountHierarchy(List<MonthlyAccountParam> monthlyAccounts, String fileNameToSave)
	{
		List<AccountData> accounts = _model.getAccounts();
		NewHierarchyOpts options = _output.accept3(accounts);
		
		if (options != null)
		{
			_model.createNewAccountHierarchy(
					options.parentAcc, options.hierarchyRoot, options.month, monthlyAccounts, fileNameToSave);		
			
		}
	}


}
