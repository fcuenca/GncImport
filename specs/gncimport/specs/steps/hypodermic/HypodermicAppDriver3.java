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
	private List<TxData> _txList;
	private AccountData _targetHierarchyRoot;
	private AccountData _sourceAccount;
	
	class InteractorFactory
	{
		private TxImportModel _model;
		
		public InteractorFactory(TxImportModel model)
		{
			_model = model;
		}
		
		public TxFileLoadInteractor txFileLoad(TxFileLoadInteractor.OutPort boundary)
		{
			return new TxFileLoadInteractor(boundary, _model);		
		}

		public AccFileLoadInteractor accFileLoad()
		{
			return new AccFileLoadInteractor(_model);
		}

		public AccSelectionInteractor accSelection(AccSelectionInteractor.OutPort boundary)
		{
			return new AccSelectionInteractor(boundary, _model);
		}

		public TxClassificationInteractor txClassification(TxClassificationInteractor.OutPort boundary)
		{
			return new TxClassificationInteractor(boundary, _model);
		}

		public TxImportInteractor txImport()
		{
			return new TxImportInteractor(_model);
		}
	}
	private InteractorFactory _interactors;
		
	public HypodermicAppDriver3(String defaultAccName, TxMatcher config)
	{
		_interactors = new InteractorFactory(GncImportApp.createAppModel(defaultAccName, config));
	}
	
	public void openCsvFile(String fileName)
	{
		TxFileLoadInteractor.OutPort boundary = new TxFileLoadInteractor.OutPort()
		{	
			@Override
			public void accept(List<TxData> txList)
			{
				_txList = txList;
			}
		};
		
		_interactors.txFileLoad(boundary).fetchTransactions(fileName);
	}
	
	public void openGncFile(String fileName)
	{	
		_interactors.accFileLoad().openGncFile(fileName); 
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
		AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort() 
		{
			@Override
			public void accept(List<AccountData> accounts)
			{
				_targetHierarchyRoot = findAccontInList(accountName, accounts);
				
				if(_targetHierarchyRoot != null)
				{
					_interactors.accSelection(this).setTargetHierarchy(_targetHierarchyRoot);
				}
				else
				{
					throw new RuntimeException("Target Hierarchy not found: " + accountName);
				}	
			}
		};
		
		_interactors.accSelection(boundary).selectAccount();	
	}

	public void selectSourceAccount(final String accountName)
	{
		AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort() 
		{
			@Override
			public void accept(List<AccountData> accounts)
			{				
				_sourceAccount = findAccontInList(accountName, accounts);

				if(_sourceAccount != null)
				{
					_interactors.accSelection(this).setSourceAccount(_sourceAccount);
				}
				else
				{
					throw new RuntimeException("Source Hierarchy not found: " + accountName);
				}		
			}
		};
		
		_interactors.accSelection(boundary).selectAccount();
	}

	public List<String> observedTagetHierarchyAccounts()
	{
		final List<String> accNames = new ArrayList<String>();
		
		TxClassificationInteractor.OutPort boundary = new TxClassificationInteractor.OutPort() 
		{
			@Override
			public void accept(List<AccountData> accounts)
			{								
				for (AccountData accountData : accounts)
				{
					accNames.add(accountData.getName());
				}
			}
		};
		
		_interactors.txClassification(boundary).getCandidateTargetAccounts();
		
		return accNames;
	}

	public List<String> observedParentsForTargetHierarchyAccounts()
	{
		final List<String> accNames = new ArrayList<String>();
				
		TxClassificationInteractor.OutPort boundary = new TxClassificationInteractor.OutPort() 
		{
			@Override
			public void accept(List<AccountData> accounts)
			{								
				for (AccountData accountData : accounts)
				{
					accNames.add(accountData.getParentId());
				}
			}
		};
		
		_interactors.txClassification(boundary).getCandidateTargetAccounts();

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
		final ArrayList<String> result = new ArrayList<String>();

		AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort() 
		{
			@Override
			public void accept(List<AccountData> accounts)
			{				
				for (AccountData accountData : accounts)
				{
					result.add(accountData.getName());
				}				
			}
		};

		_interactors.accSelection(boundary).selectAccount();

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
		_interactors.txImport().saveTxTo(_txList, gncFileName);
	}
	
	//TODO: extract utility functions that manipulate Gnc classes into different module (in GncXmlLib perhaps?)
	private AccountData findAccontInList(String accountName, List<AccountData> accounts)
	{
		for (AccountData acc : accounts)
		{
			if(acc.getName().equals(accountName))
			{
				return acc;
			}
		}
		return null;
	}
}