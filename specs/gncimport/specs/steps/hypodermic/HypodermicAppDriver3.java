package gncimport.specs.steps.hypodermic;

import gncimport.GncImportApp;
import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.InteractorFactory;
import gncimport.interactors.TxClassificationInteractor;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.models.AccountData;
import gncimport.models.TxData;
import gncimport.models.TxMatcher;

import java.util.ArrayList;
import java.util.List;

public class HypodermicAppDriver3 
{
	private InteractorFactory _interactors;
	
	private List<TxData> _txList;
	private AccountData _targetHierarchyRoot;
	private AccountData _sourceAccount;
	
		
	public HypodermicAppDriver3(String defaultAccName, TxMatcher config)
	{
		_interactors = new InteractorFactory(GncImportApp.createAppModel(defaultAccName, config));
	}
	
	public void openCsvFile(String fileName)
	{
		TxBrowseInteractor.OutPort boundary = new TxBrowseInteractor.OutPort()
		{	
			@Override
			public void accept(List<TxData> txList)
			{
				_txList = txList;
			}

			@Override
			public void fileWasOpened(String fileName)
			{
				//do nothing here
			}
		};
		
		_interactors.txBrowse(boundary).fetchTransactions(fileName);
	}
	
	public void openGncFile(String fileName)
	{	
		AccFileLoadInteractor.OutPort boundary = new AccFileLoadInteractor.OutPort() 
		{
			@Override
			public void fileWasOpened(String fileName)
			{
				// do nothing for now
			}
		};
		_interactors.accFileLoad(boundary).openGncFile(fileName); 
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
				_targetHierarchyRoot = findAccountInList(accountName, accounts);
				
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
		
		_interactors.accSelection(boundary).getAccounts();	
	}

	public void selectSourceAccount(final String accountName)
	{
		AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort() 
		{
			@Override
			public void accept(List<AccountData> accounts)
			{				
				_sourceAccount = findAccountInList(accountName, accounts);

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
		
		_interactors.accSelection(boundary).getAccounts();
	}

	public List<String> observedTagetHierarchyAccounts()
	{
		final List<String> accNames = new ArrayList<String>();
		
		TxClassificationInteractor.OutPort boundary = new TxClassificationInteractor.OutPort() 
		{
			@Override
			public void accept(List<AccountData> accounts)
			{								
				addAccNamesToList(accounts, accNames);
			}
		};
		
		_interactors.txClassification(boundary).getCandidateTargetAccounts();
		
		return accNames;
	}

	public List<String> observedParentsForTargetHierarchyAccounts()
	{
		final List<String> parentIds = new ArrayList<String>();
				
		TxClassificationInteractor.OutPort boundary = new TxClassificationInteractor.OutPort() 
		{
			@Override
			public void accept(List<AccountData> accounts)
			{								
				addParentIdsToList(accounts, parentIds);
			}
		};
		
		_interactors.txClassification(boundary).getCandidateTargetAccounts();

		return parentIds;
	}

	public List<String> observedAccountList()
	{
		final ArrayList<String> accNames = new ArrayList<String>();

		AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort() 
		{
			@Override
			public void accept(List<AccountData> accounts)
			{			
				addAccNamesToList(accounts, accNames);
			}
		};

		_interactors.accSelection(boundary).getAccounts();

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
	
	public void editTxDescription(String originalDesc, String editedDesc)
	{
		for (TxData txData : _txList)
		{
			if(txData.description.matches(originalDesc))
			{
				txData.description = editedDesc;
			}
		}
	}
	
	public void setAccountForTransactionsMatching(final String accName, final String txDesc)
	{
		final ArrayList<AccountData> newAccount = new ArrayList<AccountData>();
		
		TxClassificationInteractor.OutPort boundary = new TxClassificationInteractor.OutPort() 
		{
			@Override
			public void accept(List<AccountData> accounts)
			{	
				for (AccountData acc : accounts)
				{
					if(acc.getName().equals(accName))
					{
						newAccount.add(acc);
						break;
					}
				}
			}
		};
		
		_interactors.txClassification(boundary).getCandidateTargetAccounts();
		
		for (TxData txData : _txList)
		{
			if(txData.description.matches(txDesc))
			{
				txData.targetAccount = newAccount.get(0);
			}
		}
	}

	//TODO: extract utility functions that manipulate Gnc classes into different module (in GncXmlLib perhaps?)
	private AccountData findAccountInList(String accountName, List<AccountData> accounts)
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

	private void addAccNamesToList(List<AccountData> accounts, final List<String> result)
	{
		for (AccountData accountData : accounts)
		{
			result.add(accountData.getName());
		}
	}

	private void addParentIdsToList(List<AccountData> accounts, final List<String> result)
	{
		for (AccountData accountData : accounts)
		{
			result.add(accountData.getParentId());
		}
	}

	public List<TxData> observedTxData()
	{
		return _txList;
	}
}