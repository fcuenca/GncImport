package gncimport.interactors;

import java.util.Date;
import java.util.List;

import gncimport.models.TxImportModel;
import gncimport.transfer.AccountData;
import gncimport.transfer.TxData;

public class TxBrowseInteractor
{
	public interface OutPort
	{
		void accept(List<TxData> txList, List<AccountData> theAccList);
		void fileWasOpened(String fileName);
	}
	
	private TxImportModel _model;
	private OutPort _output;
	
	public TxBrowseInteractor(OutPort output, TxImportModel model)
	{
		_model = model;
		_output = output;
	}

	public void fetchTransactions(String fileName)
	{
		_output.accept(_model.fetchTransactionsFrom(fileName), _model.getCandidateTargetAccounts());
		_output.fileWasOpened(fileName);
	}

	public void filterTxList(Date lowerBound, Date upperBound)
	{
		_output.accept(_model.filterTxList(lowerBound, upperBound), _model.getCandidateTargetAccounts());
	}

}