package gncimport.ui;

import gncimport.interactors.TxImportInteractor;

public class SaveGncCommand implements Command<SaveGncEvent>
{
	private TxView _theView;
	private TxImportInteractor _theInteractor;

	public SaveGncCommand(TxView view, TxImportInteractor interactor)
	{
		this._theView = view;
		this._theInteractor = interactor;
	}

	@Override
	public void execute(SaveGncEvent event)
	{
		try
		{
			_theInteractor.saveTxTo(_theView.getTxTableModel().getTransactions(), event.fileName);
		}
		catch (Exception e)
		{
			_theView.handleException(e);
		}
	}		
}