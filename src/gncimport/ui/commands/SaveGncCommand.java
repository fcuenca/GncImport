package gncimport.ui.commands;

import gncimport.interactors.TxImportInteractor;
import gncimport.ui.Command;
import gncimport.ui.TxView;
import gncimport.ui.events.SaveGncEvent;

public class SaveGncCommand 
	extends AbstractCommand<SaveGncEvent> implements Command<SaveGncEvent>
{
	private TxImportInteractor _theInteractor;

	public SaveGncCommand(TxView view, TxImportInteractor interactor)
	{
		super(view);
		this._theInteractor = interactor;
	}

	@Override
	protected void doExecute(SaveGncEvent event)
	{
		_theInteractor.saveTxTo(_theView.getTxTableModel().getTransactions(), event.fileName);
	}		
}