package gncimport.ui;

import gncimport.interactors.AccSelectionInteractor;
import gncimport.models.AccountData;

class SelectExpenseAccCommand implements Command<SelectExpenseAccEvent>
{
	private TxView _theView;
	private AccSelectionInteractor _theInteractor;

	public SelectExpenseAccCommand(TxView view, AccSelectionInteractor interactor)
	{
		this._theView = view;
		this._theInteractor = interactor;
	}

	@Override
	public void execute(SelectExpenseAccEvent args)
	{
		AccountData result = args.originalAcc;

		try
		{			
			result = chooseExpenseAccount(args);
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

	private AccountData chooseExpenseAccount(SelectExpenseAccEvent args)
	{
		if (args.newAcc.equals(CandidateAccList.OTHER_ACC_PLACEHOLDER))
		{
			AccountData selectedAcc = _theInteractor.browseAccounts();

			if (selectedAcc != null)
			{
				return selectedAcc;
			}
			else return args.originalAcc;
		}
		
		return args.newAcc;
	}
}