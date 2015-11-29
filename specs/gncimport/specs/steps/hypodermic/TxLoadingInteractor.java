package gncimport.specs.steps.hypodermic;

import gncimport.models.TxImportModel;

public class TxLoadingInteractor
{
	private TxImportModel _model;
	private TxLoadingOutputBoundary _output;

	public TxLoadingInteractor(TxLoadingOutputBoundary output, TxImportModel model)
	{
		_model = model;
		_output = output;
	}

	public void fetchTransactions(String fileName)
	{
		_output.setResponse(_model.fetchTransactionsFrom(fileName));
	}

}