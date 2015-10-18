package gncimport.specs.steps.hypodermic;

import gncimport.GncImportApp;
import gncimport.models.AccountData;
import gncimport.models.TxData;
import gncimport.models.TxImportModel;
import gncimport.models.TxMatcher;

import java.util.List;

public class HypodermicAppDriver2
{
	private TxImportModel _model;
	private List<TxData> _txList;
		
	public HypodermicAppDriver2(String defaultAccName, TxMatcher config)
	{
		_model = GncImportApp.createAppModel(defaultAccName, config);
	}
	
	public void openCsvFile(String fileName)
	{
		_txList = _model.fetchTransactionsFrom(fileName);
	}
	
	public void openGncFile(String fileName)
	{	
		_model.openGncFile(fileName);
	}

	public int observedTxCount()
	{
		return _txList.size();
	}
	
	public String observedTxAtRow(int i)
	{
		return _txList.get(i).description;
	}

	public String observedAccountAtRow(int i)
	{
		return _txList.get(i).targetAccount.getName();
	}

	public void selectTargetAccHierarchy(String accountName)
	{
		List<AccountData> accounts = _model.getAccounts();
		
		for (AccountData acc : accounts)
		{
			if(acc.getName().equals(accountName))
			{
				_model.setTargetHierarchy(acc);				
			}
		}
	}

}