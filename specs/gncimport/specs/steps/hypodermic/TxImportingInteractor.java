package gncimport.specs.steps.hypodermic;

import gncimport.models.TxData;
import gncimport.models.TxImportModel;

import java.util.List;

public class TxImportingInteractor
{
	private TxImportModel _model;

	public TxImportingInteractor(TxImportModel model)
	{
		_model = model;
	}

	public void saveTxTo(List<TxData> txList, String gncFileName)
	{
		_model.saveTxTo(txList, gncFileName);
	}

}
