package gncimport.specs.steps.hypodermic;

import gncimport.ConfigOptions;
import gncimport.GncImportApp;
import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.PropertyEditInteractor;
import gncimport.interactors.AccSelectionInteractor.NewHierarchyOpts;
import gncimport.interactors.InteractorFactory;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.tests.unit.ListUtils;
import gncimport.transfer.AccountData;
import gncimport.transfer.Month;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleCategory;
import gncimport.transfer.RuleTester;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.TxData;
import gncimport.transfer.UserEnteredMatchingRule;
import gncimport.utils.ProgrammerError;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class HypodermicAppDriver3 
{
	private InteractorFactory _interactors;
	private ConfigOptions _config;
	
	private List<TxData> _txList;
	private AccountData _targetHierarchyRoot;
	private AccountData _sourceAccount;
	private List<AccountData> _candidateAccList;
	
	private List<MatchingRule> _observedIgnoreRules = new ArrayList<MatchingRule>();	
	private List<OverrideRule> _observedAccOverrideRules = new ArrayList<OverrideRule>();	

	class AccSelectionOutput implements AccSelectionInteractor.OutPort
	{
		@Override
		public void sourceAccHasBeenSet(String accName)
		{
			// not used here
		}
		
		@Override
		public NewHierarchyOpts promptForNewHierarchy(List<AccountData> accounts)
		{
			throw new ProgrammerError("overriden in subclasses");
		}

		@Override
		public AccountData selectAccount(List<AccountData> accounts)
		{
			throw new ProgrammerError("overriden in subclasses");
		}

		@Override
		public void targetHierarchyHasBeenSet(String accName, List<AccountData> candidateAccList)
		{	
			throw new ProgrammerError("overriden in subclasses");
		}
	}


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
			//not used here
		}
	};
	
	public HypodermicAppDriver3(String defaultAccName, ConfigOptions config)
	{
		_config = config;
		_interactors = new InteractorFactory(
				GncImportApp.createAppModel(defaultAccName, _config), config);
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
		AccSelectionInteractor.OutPort boundary = new AccSelectionOutput() 
		{
			@Override
			public void targetHierarchyHasBeenSet(String accName, List<AccountData> candidateAccList)
			{
				_candidateAccList = candidateAccList;
			}

			@Override
			public AccountData selectAccount(List<AccountData> accounts)
			{
				_targetHierarchyRoot = findFirstAccWithNameInList(accountName, accounts);
				_candidateAccList = null;
				return _targetHierarchyRoot;
			}
		};
		
		_interactors.accSelection(boundary).selectTargetAccount();	
	}

	public void selectSourceAccount(final String accountName)
	{
		AccSelectionInteractor.OutPort boundary = new AccSelectionOutput() 
		{
			@Override
			public AccountData selectAccount(List<AccountData> accounts)
			{
				_sourceAccount = findFirstAccWithNameInList(accountName, accounts);
				return _sourceAccount;
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

		AccSelectionInteractor.OutPort boundary = new AccSelectionOutput() 
		{
			@Override
			public AccountData selectAccount(List<AccountData> accounts)
			{
				addAccNamesToList(accounts, accNames);
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
			final String newAccName, String fileNameToSave)
	{
		AccSelectionInteractor.OutPort boundary = new AccSelectionOutput() 
		{
			@Override
			public NewHierarchyOpts promptForNewHierarchy(List<AccountData> accounts)
			{
				AccountData parent = findLastSubAccountInChain(pathToParentAcc, accounts);
				return new NewHierarchyOpts(parent, newAccName, new Month(month));
			}
		};
		
		_interactors.accSelection(boundary).createNewAccountHierarchy(_config.getMonthlyAccounts(), fileNameToSave);
	}
	
	public void browseCurrentProperties()
	{
		PropertyEditInteractor.OutPort boundary = new PropertyEditInteractor.OutPort()
		{
			@SuppressWarnings("unchecked")
			@Override
			public boolean editProperties(Map<RuleCategory, Object> allRules, RuleTester tester)
			{
				_observedIgnoreRules = new ArrayList<MatchingRule>((List<MatchingRule>) allRules.get(RuleCategory.ignore));
				_observedAccOverrideRules = new ArrayList<OverrideRule>((List<OverrideRule>)allRules.get(RuleCategory.acc_override));
				
				return false;
			}
		};
		
		_interactors.propertyEdit(boundary).editProperties();
	}

	public void editIgnoreRules(final List<String> newRules)
	{		
		PropertyEditInteractor.OutPort boundary = new PropertyEditInteractor.OutPort()
		{
			@SuppressWarnings("unchecked")
			@Override
			public boolean editProperties(Map<RuleCategory, Object> allRules,RuleTester tester)
			{
				_observedIgnoreRules = new ArrayList<MatchingRule>((List<MatchingRule>) allRules.get(RuleCategory.ignore));
				_observedAccOverrideRules = new ArrayList<OverrideRule>((List<OverrideRule>)allRules.get(RuleCategory.acc_override));

				((List<MatchingRule>) allRules.get(RuleCategory.ignore)).clear();
				
				for (String rule : newRules)
				{
					((List<MatchingRule>) allRules.get(RuleCategory.ignore)).add(new UserEnteredMatchingRule(rule));					
				}
				
				return true;
			}
		};
		
		_interactors.propertyEdit(boundary).editProperties();
	}
	
	public void editAccOverrideRules(final List<Map<String, String>> newRules)
	{
		PropertyEditInteractor.OutPort boundary = new PropertyEditInteractor.OutPort()
		{
			@SuppressWarnings("unchecked")
			@Override
			public boolean editProperties(Map<RuleCategory, Object> allRules, RuleTester tester)
			{
				_observedIgnoreRules = new ArrayList<MatchingRule>((List<MatchingRule>) allRules.get(RuleCategory.ignore));
				_observedAccOverrideRules = new ArrayList<OverrideRule>((List<OverrideRule>)allRules.get(RuleCategory.acc_override));

				((List<OverrideRule>) allRules.get(RuleCategory.acc_override)).clear();

				for (Map<String, String> map : newRules)
				{
					String desc = map.get("Description");
					String account = map.get("Account");
					
					((List<OverrideRule>) allRules.get(RuleCategory.acc_override)).add(new OverrideRule(desc, account));	
				}
				
				return true;
			}
		};
		
		_interactors.propertyEdit(boundary).editProperties();

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

	public Properties getProperties()
	{
		return _config.getProperties();
	}

	public List<String> observedIgnoreRules()
	{
		List<String> rules = new ArrayList<String>();
		
		for (MatchingRule rule : _observedIgnoreRules)
		{
			rules.add(rule.text());
		}
		
		return rules;
	}

	public List<List<String>> observedAccountOverrideRules()
	{
		List<List<String>> rules = new ArrayList<List<String>>();
		
		for (OverrideRule rule : _observedAccOverrideRules)
		{
			rules.add(new ArrayList<String>(ListUtils.list_of(rule.textToMatch.text(), rule.override.text())));
		}
		
		return rules;
	}
}