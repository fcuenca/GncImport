package gncimport.specs.steps.hypodermic;

import java.util.List;

import gncimport.models.TxData;
import gncimport.models.TxImportModel;

public class TxFileLoadInteractor
{
	public interface OutPort
	{
		void accept(List<TxData> txList);
	}
	
	private TxImportModel _model;
	private OutPort _output;
	
	public TxFileLoadInteractor(OutPort output, TxImportModel model)
	{
		_model = model;
		_output = output;
	}

	public void fetchTransactions(String fileName)
	{
		_output.accept(_model.fetchTransactionsFrom(fileName));
	}

}