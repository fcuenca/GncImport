package gncimport.ui;

import gncimport.interactors.AccSelectionInteractor;
import gncimport.models.AccountData;

class SelectExpenseAccCommand
{
	private AccountData _newAcc;
	private AccountData _originalAcc;
	private TxView _theView;
	private AccSelectionInteractor _theInteractor;

	public SelectExpenseAccCommand(AccountData newAcc, AccountData originalAcc, TxView view, AccSelectionInteractor interactor)
	{
		this._newAcc = newAcc;
		this._originalAcc = originalAcc;
		this._theView = view;
		this._theInteractor = interactor;
	}

	public AccountData execute()
	{
		AccountData result = _newAcc;
		
		if (_newAcc.equals(CandidateAccList.OTHER_ACC_PLACEHOLDER))
		{
			result = _originalAcc;
			
			try
			{				
				AccountData selectedAcc = _theInteractor.browseAccounts();
	
				if (selectedAcc != null)
				{
					result = selectedAcc;
				}
			}
			catch (Exception e)
			{
				_theView.handleException(e);
			}
		}
		
		_theView.selectExpenseAccForTx(result);
		return result;
	}
}