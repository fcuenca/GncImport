package gncimport.interactors;

import gncimport.models.TxImportModel;
import gncimport.transfer.TxData;

import java.util.List;

public class TxImportInteractor
{
	private TxImportModel _model;

	public TxImportInteractor(TxImportModel model)
	{
		_model = model;
	}

	public void saveTxTo(List<TxData> txList, String gncFileName)
	{
		_model.saveTxTo(txList, gncFileName);
	}

}
