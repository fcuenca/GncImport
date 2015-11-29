package gncimport.specs.steps.hypodermic;

import gncimport.GncImportApp;
import gncimport.models.AccountData;
import gncimport.models.TxData;
import gncimport.models.TxImportModel;
import gncimport.models.TxMatcher;

import java.util.ArrayList;
import java.util.List;

public class HypodermicAppDriver3 
{
	private TxImportModel _model;
	private List<TxData> _txList;
	private AccountData _targetHierarchyRoot;
	private AccountData _sourceAccount;
	
	class UseCaseFactory
	{
		private TxImportModel _model;
		
		public UseCaseFactory(TxImportModel model)
		{
			_model = model;
		}
		
		public TxLoadingInteractor startTxLoading(TxLoadingOutputBoundary boundary)
		{
			return new TxLoadingInteractor(boundary, _model);		
		}

		public AccFileLoadingInteractor startAccLoading()
		{
			return new AccFileLoadingInteractor(_model);
		}

		public AccSelectionInteractor startAccSelection(AccSelectionOutputBoundary boundary)
		{
			return new AccSelectionInteractor(boundary, _model);
		}
	}
	private UseCaseFactory _useCases;
		
	public HypodermicAppDriver3(String defaultAccName, TxMatcher config)
	{
		_model = GncImportApp.createAppModel(defaultAccName, config);
		_useCases = new UseCaseFactory(_model);
	}
	
	public void openCsvFile(String fileName)
	{
		TxLoadingOutputBoundary boundary = new TxLoadingOutputBoundary()
		{	
			@Override
			public void setResponse(List<TxData> txList)
			{
				_txList = txList;
			}
		};
		
		_useCases.startTxLoading(boundary).fetchTransactions(fileName);
	}
	
	public void openGncFile(String fileName)
	{	
		_useCases.startAccLoading().openGncFile(fileName);
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

	public void selectTargetAccHierarchy(final String accountName)
	{		
		AccSelectionOutputBoundary boundary = new AccSelectionOutputBoundary() 
		{

			@Override
			public void setResponse(List<AccountData> accounts)
			{
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
					_useCases.startAccSelection(this).setTargetHierarchy(_targetHierarchyRoot);
				}
				else
				{
					throw new RuntimeException("Target Hierarchy not found: " + accountName);
				}
				
			}
		};
		
		_useCases.startAccSelection(boundary).selectAccount();
		
	}
	
	//TODO: remove this duplication
	public void selectSourceAccount(final String accountName)
	{
		AccSelectionOutputBoundary boundary = new AccSelectionOutputBoundary() 
		{
			@Override
			public void setResponse(List<AccountData> accounts)
			{
				_sourceAccount = null;
				
				for (AccountData acc : accounts)
				{
					if(acc.getName().equals(accountName))
					{
						_sourceAccount = acc;
						break;
					}
				}
				
				if(_sourceAccount != null)
				{
					_useCases.startAccSelection(this).setSourceAccount(_sourceAccount);
				}
				else
				{
					throw new RuntimeException("Source Hierarchy not found: " + accountName);
				}	
				
			}
		};
		
		_useCases.startAccSelection(boundary).selectAccount();
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

	public int observedIgnoreCount()
	{
		int count = 0;
		
		for (TxData tx : _txList)
		{
			if(tx.doNotImport)
			{
				count++;
			}
		}
		
		return count;
	}

	public void importTransactionsTo(String gncFileName)
	{
		_model.saveTxTo(_txList, gncFileName);
	}
}