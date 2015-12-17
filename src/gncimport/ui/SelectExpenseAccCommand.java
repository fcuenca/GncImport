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
		if (!_newAcc.equals(CandidateAccList.OTHER_ACC_PLACEHOLDER))
		{
			return _newAcc;
		}

		try
		{				
			AccountData selectedAcc = _theInteractor.browseAccounts();

			if (selectedAcc != null)
			{
				return selectedAcc;
			}
			else
			{
				return _originalAcc;
			}
		}
		catch (Exception e)
		{
			_theView.handleException(e);
			return _originalAcc;
		}
	}
}