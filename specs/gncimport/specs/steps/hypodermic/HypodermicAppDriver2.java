package gncimport.specs.steps.hypodermic;

import gncimport.GncImportApp;
import gncimport.models.AccountData;
import gncimport.models.TxData;
import gncimport.models.TxImportModel;
import gncimport.models.TxMatcher;

import java.util.ArrayList;
import java.util.List;

public class HypodermicAppDriver2
{
	private TxImportModel _model;
	private List<TxData> _txList;
	private AccountData _targetHierarchyRoot;
		
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
		
		_targetHierarchyRoot = null;
		
		for (AccountData acc : accounts)
		{
			if(acc.getName().equals(accountName))
			{
				_targetHierarchyRoot = acc;
				break;
			}
		}
		
		if(_targetHierarchyRoot != null)
		{
			_model.setTargetHierarchy(_targetHierarchyRoot);							
		}
		else
		{
			throw new RuntimeException("Target Hierarchy not found: " + accountName);
		}
	}

	public List<String> observedTagetHierarchyAccounts()
	{
		List<String> accNames = new ArrayList<String>();
		
		List<AccountData> acounts = _model.getCandidateTargetAccounts();
		
		for (AccountData accountData : acounts)
		{
			accNames.add(accountData.getName());
		}
		
		return accNames;
	}

	public List<String> observedParentsForTargetHierarchyAccounts()
	{
		List<String> accNames = new ArrayList<String>();
		
		List<AccountData> acounts = _model.getCandidateTargetAccounts();
		
		for (AccountData accountData : acounts)
		{
			accNames.add(accountData.getParentId());
		}
		
		return accNames;
	}

	public String observedIdForTargetHierarchyRoot()
	{
		if(_targetHierarchyRoot != null)
		{
			return _targetHierarchyRoot.getId();
		}
		else
		{
			throw new RuntimeException("Target hierarchy hasn't been set yet!");
		}
	}

	public List<String> observedAccountList()
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for (AccountData accountData : _model.getAccounts())
		{
			result.add(accountData.getName());
		}
		
		return result;
	}
}