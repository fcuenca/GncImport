package gncimport.specs.steps.hypodermic;

import gncimport.GncImportApp;
import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.AccSelectionInteractor.NewHierarchyOpts;
import gncimport.interactors.InteractorFactory;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.models.AccountData;
import gncimport.models.Month;
import gncimport.models.MonthlyAccountParam;
import gncimport.models.TxData;
import gncimport.models.TxMatcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HypodermicAppDriver3 
{
	private InteractorFactory _interactors;
	
	private List<TxData> _txList;
	private AccountData _targetHierarchyRoot;
	private AccountData _sourceAccount;
	private List<AccountData> _candidateAccList =  new ArrayList<AccountData>();

	private TxBrowseInteractor.OutPort 	txBrowseOutput = new TxBrowseInteractor.OutPort()
	{	
		@Override
		public void accept(List<TxData> txList, List<AccountData> accList)
		{
			_txList = txList;
		}

		@Override
		public void fileWasOpened(String fileName)
		{
			//do nothing here
		}
	};
	
		
	public HypodermicAppDriver3(String defaultAccName, TxMatcher config)
	{
		_interactors = new InteractorFactory(GncImportApp.createAppModel(defaultAccName, config));
	}
	
	public void openCsvFile(String fileName)
	{		
		_interactors.txBrowse(txBrowseOutput).fetchTransactions(fileName);
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
	
	public List<TxData> observedTxData()
	{
		return _txList;
	}

	public void selectTargetAccHierarchy(final String accountName)
	{		
		AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort() 
		{
			@Override
			public void targetHierarchyHasBeenSet(String accName, List<AccountData> candidateAccList)
			{
				_candidateAccList = candidateAccList;
			}

			@Override
			public void sourceAccHasBenSet(String accName)
			{
				//Do nothing for now
			}

			@Override
			public AccountData selectAccount(List<AccountData> accounts)
			{
				_targetHierarchyRoot = findFirstAccWithNameInList(accountName, accounts);
				_candidateAccList = new ArrayList<AccountData>();
				return _targetHierarchyRoot;
			}

			@Override
			public NewHierarchyOpts promptForNewHierarchy(List<AccountData> accounts)
			{
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		_interactors.accSelection(boundary).selectTargetAccount();	
	}

	public void selectSourceAccount(final String accountName)
	{
		AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort() 
		{
			@Override
			public void targetHierarchyHasBeenSet(String accName, List<AccountData> candidateAccList)
			{
				// Do nothing for now
			}

			@Override
			public void sourceAccHasBenSet(String accName)
			{
				//Do nothing for now
			}

			@Override
			public AccountData selectAccount(List<AccountData> accounts)
			{
				_sourceAccount = findFirstAccWithNameInList(accountName, accounts);
				return _sourceAccount;
			}

			@Override
			public NewHierarchyOpts promptForNewHierarchy(List<AccountData> accounts)
			{
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		_interactors.accSelection(boundary).selectSourceAccount();
	}

	public List<String> observedTagetHierarchyAccounts()
	{
		List<String> accNames = new ArrayList<String>();

		addAccNamesToList(_candidateAccList, accNames);

		return accNames;
	}

	public List<String> observedParentsForTargetHierarchyAccounts()
	{
		 List<String> parentIds = new ArrayList<String>();
				
		addParentIdsToList(_candidateAccList, parentIds);
		
		return parentIds;
	}

	public List<String> observedAccountList()
	{
		final ArrayList<String> accNames = new ArrayList<String>();

		AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort() 
		{
			@Override
			public void targetHierarchyHasBeenSet(String accName, List<AccountData> candidateAccList)
			{
				// Do nothing for now
			}

			@Override
			public void sourceAccHasBenSet(String accName)
			{
				//Do nothing for now
			}

			@Override
			public AccountData selectAccount(List<AccountData> accounts)
			{
				addAccNamesToList(accounts, accNames);
				return null;
			}

			@Override
			public NewHierarchyOpts promptForNewHierarchy(List<AccountData> accounts)
			{
				// TODO Auto-generated method stub
				return null;
			}
		};

		_interactors.accSelection(boundary).browseAccounts();

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
		AccountData newAccount = findFirstAccWithNameInList(accName, _candidateAccList);
		for (TxData txData : _txList)
		{
			if(txData.description.matches(txDesc))
			{
				txData.targetAccount = newAccount;
			}
		}
	}
	
	public void ignoreTransactionsMatching(String txDescRegex)
	{
		for (TxData txData : _txList)
		{
			if(txData.description.matches(txDescRegex))
			{
				txData.doNotImport = true;
			}
		}
	}

	public void filterTransactions(Date start, Date end)
	{
		_interactors.txBrowse(txBrowseOutput).filterTxList(start, end);
	}

	public void createAccounts(final String month, final List<String> pathToParentAcc,
			final String newAccName, List<MonthlyAccountParam> subAccountList, String fileNameToSave)
	{
		AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort() 
		{
			@Override
			public void targetHierarchyHasBeenSet(String accName, List<AccountData> candidateAccList)
			{
				// Do nothing for now
			}

			@Override
			public void sourceAccHasBenSet(String accName)
			{
				//Do nothing for now				
			}

			@Override
			public AccountData selectAccount(List<AccountData> accounts)
			{
				return null; // not used here
			}

			@Override
			public NewHierarchyOpts promptForNewHierarchy(List<AccountData> accounts)
			{
				AccountData parent = findLastSubAccountInChain(pathToParentAcc, accounts);
				return new NewHierarchyOpts(parent, newAccName, new Month(month));
			}
		};
		
		_interactors.accSelection(boundary).createNewAccountHierarchy(subAccountList, fileNameToSave);
	}

	//TODO: extract utility functions that manipulate Gnc classes into different module (in GncXmlLib perhaps?)
	private AccountData findFirstAccWithNameInList(String accountName, List<AccountData> accounts)
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
	
	private AccountData findLastSubAccountInChain(List<String> parentAccChain, List<AccountData> accounts)
	{
		AccountData parent = findFirstAccWithNameInList(parentAccChain.get(0), accounts);
		
		if(parent != null)
		{
			for (AccountData acc : accounts)
			{
				//TODO: generalize for a list of more than 2 accounts
				if(acc.getParentId() != null && acc.getParentId().equals(parent.getId()) && 
						acc.getName().equals(parentAccChain.get(1)))
				{
					return acc;
				}
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
}