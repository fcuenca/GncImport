package gncimport.specs.steps.hypodermic;

import gncimport.models.TxImportModel;

public class ImportInteractor
{
	private TxImportModel _model;
	private ImportOutputBoundary _output;

	public ImportInteractor(ImportOutputBoundary output, TxImportModel model)
	{
		_model = model;
		_output = output;
	}

	public void fetchTransactions(String fileName)
	{
		_output.setResponse(_model.fetchTransactionsFrom(fileName));
	}

}