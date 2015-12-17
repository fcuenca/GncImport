package gncimport.ui;

import gncimport.interactors.TxImportInteractor;

class SaveGncCommand
{
	private String _fileName;
	private TxView _theView;
	private TxImportInteractor _theInteractor;

	public SaveGncCommand(String fileName, TxView view, TxImportInteractor interactor)
	{
		this._fileName = fileName;
		this._theView = view;
		this._theInteractor = interactor;
	}

	public void execute()
	{
		try
		{
			_theInteractor.saveTxTo(_theView.getTxTableModel().getTransactions(), _fileName);
		}
		catch (Exception e)
		{
			_theView.handleException(e);
		}
	}		
}