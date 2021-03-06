package gncimport.interactors;

import gncimport.models.TxImportModel;
import gncimport.transfer.AccountData;
import gncimport.transfer.Month;
import gncimport.transfer.MonthlyAccount;

import java.util.List;

public class AccSelectionInteractor
{
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

	public interface OutPort
	{
		AccountData selectAccount(List<AccountData> accounts);
		NewHierarchyOpts promptForNewHierarchy(List<AccountData> accounts); 
		void targetHierarchyHasBeenSet(String accName, List<AccountData> candidateAccList);
		void sourceAccHasBeenSet(String accName);
	}

	private TxImportModel _model;
	private OutPort _output;

	public AccSelectionInteractor(OutPort output, TxImportModel model)
	{
		_model = model;
		_output = output;
	}

	public void selectSourceAccount()
	{
		AccountData selectedAccount = browseAccounts();

		if (selectedAccount != null)
		{
			_model.setSourceAccount(selectedAccount);	
			_output.sourceAccHasBeenSet(selectedAccount.getName());
		}
	}

	public void selectTargetAccount()
	{
		AccountData selectedAccount = browseAccounts();

		if (selectedAccount != null)
		{
			_model.setTargetHierarchy(selectedAccount);	
			_output.targetHierarchyHasBeenSet(selectedAccount.getName(), _model.getCandidateTargetAccounts());
		}
	}

	public AccountData browseAccounts()
	{
		List<AccountData> accounts = _model.getAccounts();
		AccountData selectedAccount = _output.selectAccount(accounts);
				
		return selectedAccount;
	}
		
	public void createNewAccountHierarchy(List<MonthlyAccount> monthlyAccounts, String fileNameToSave)
	{
		List<AccountData> accounts = _model.getAccounts();
		NewHierarchyOpts options = _output.promptForNewHierarchy(accounts);
		
		if (options != null)
		{
			_model.createNewAccountHierarchy(
					options.parentAcc, options.hierarchyRoot, options.month, monthlyAccounts, fileNameToSave);		
			
		}
	}
}
