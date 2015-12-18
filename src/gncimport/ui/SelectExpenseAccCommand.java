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

	public void execute()
	{
		AccountData result = _originalAcc;

		try
		{			
			result = chooseExpenseAccount();
		}
		catch (Exception e)
		{
			_theView.handleException(e);
		}

		// Hmm.... This is a weird command, because it sets the output directly
		// rather than as an output of the Interactor. The functionality
		// required here is purely the result of the design of the Swing UI (in particular,
		// the way the ComboBox editor works), so it makes sense to keep it here.
		// But.... this doesn't smell right .... :-/
		_theView.selectExpenseAccForTx(result);
	}

	private AccountData chooseExpenseAccount()
	{
		if (_newAcc.equals(CandidateAccList.OTHER_ACC_PLACEHOLDER))
		{
			AccountData selectedAcc = _theInteractor.browseAccounts();

			if (selectedAcc != null)
			{
				return selectedAcc;
			}
			else return _originalAcc;
		}
		
		return _newAcc;
	}
}