package gncimport.ui;

import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.InteractorFactory;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.models.AccountData;
import gncimport.models.TxData;
import gncimport.models.TxImportModel;
import gncimport.ui.TxView.NewHierarchyParams;
import gncimport.utils.ProgrammerError;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class MainWindowPresenter implements MainWindowRenderer
{
	public static final AccountData OTHER_ACC_PLACEHOLDER = new AccountData("<< OTHER >>", "-1");

	private final TxView _view;
	private final UIConfig _config;
	
	private final InteractorFactory _interactors;

	public MainWindowPresenter(TxImportModel model, TxView view, UIConfig config)
	{
		this._interactors = new InteractorFactory(model);

		this._view = view;
		this._config = config;
	}
	
	//TODO
	class MyAccSelectionInteractorOutPort implements AccSelectionInteractor.OutPort
	{
		public AccountData selectedAccount;
		public NewHierarchyParams params; 

		
		@Override
		public void accept(List<AccountData> accounts)
		{
			selectedAccount = null;
			
			AccountTreeBuilder builder = new AccountTreeBuilder();
			
			for (AccountData account : accounts)
			{
				builder.addNodeFor(account);
			}
			
			DefaultMutableTreeNode accountRoot = builder.getRoot();				
			params = _view.promptForNewHierarchy(accountRoot);
			
			if (params != null)
			{
				if (params.parentNode == null || params.rootAccName == null || params.rootAccName.trim().isEmpty())
				{
					throw new ProgrammerError("Invalid values for new Hierarchy came through!!");
				}
				
				selectedAccount = (AccountData) params.parentNode.getUserObject();				
			}
		}
		
		@Override
		public void targetHierarchyHasBeenSet(String accName, List<AccountData> candidateAccList)
		{
			_view.displayTargetHierarchy(accName);	
			_view.updateCandidateTargetAccountList(buildCandidateAccList(candidateAccList));
		}

		@Override
		public void sourceAccHasBenSet(String accName)
		{
			_view.displaySourceAccount(accName);
		}
	}
	
	MyAccSelectionInteractorOutPort accSelectionResponse = new MyAccSelectionInteractorOutPort();	
	
	
	TxBrowseInteractor.OutPort txBrowseResponse = new TxBrowseInteractor.OutPort() 
	{
		@Override
		public void accept(List<TxData> txList, List<AccountData> theAccList)
		{
			_view.displayTxData(new TxTableModel(txList), buildCandidateAccList(theAccList));
			_view.displayTxCount(txList.size());
		}

		@Override
		public void fileWasOpened(String fileName)
		{
			_config.setLastCsvDirectory(new File(fileName).getParent());
			_view.updateCsvFileLabel(fileName);
		}
	};
	

	AccFileLoadInteractor.OutPort accFileLoadReponse = new AccFileLoadInteractor.OutPort ()
	{
		@Override
		public void fileWasOpened(String fileName)
		{
			_view.updateGncFileLabel(fileName);
			_config.setLastGncDirectory(new File(fileName).getParent());						
		}
	};
	
	// -- renderer overrides
		
	@Override
	public void onReadFromCsvFile()
	{
		loadCsv_execute(_view);
	}
	
	@Override
	public void onFilterTxList(Date fromDate, Date toDate)
	{
		filter_execute(fromDate, toDate);				
	}
	
	@Override
	public void onSaveToGncFile(String fileName)
	{
		saveGnc_execute(fileName, _view);
	}

	@Override
	public void onLoadGncFile()
	{
		loadGnc_execute(_view);
	}
	
	@Override
	public void onSelectSourceAccount()
	{
		selectSource_execute(_view);
	}

	@Override
	public void onSelectTargetHierarchy()
	{
		selectTarget_execute(_view);
	}

	@Override
	public AccountData onTargetAccountSelected(AccountData newAcc, AccountData originalAcc)
	{
		return accSelection_execute(newAcc, originalAcc, _view);
	}

	@Override
	public void onCreateNewAccHierarchy(String fileNameToSave)
	{	
		createAcc_execute(fileNameToSave, _view);
	}

	// -- private utility funcs
	
	private AccountData selectAccountFromTree()
	{
		final ArrayList<AccountData> result = new ArrayList<AccountData>();
		
		AccSelectionInteractor.OutPort boundary = new MyAccSelectionInteractorOutPort()
		{
			@Override
			public void accept(List<AccountData> accounts)
			{
				AccountTreeBuilder builder = new AccountTreeBuilder();
				
				for (AccountData account : accounts)
				{
					builder.addNodeFor(account);
				}
				
				DefaultMutableTreeNode selectedNode = _view.promptForAccount(builder.getRoot());

				if (selectedNode != null)
				{
					AccountData selectedAccount = (AccountData) selectedNode.getUserObject();
					result.add(selectedAccount);
				}
			}
		};

		_interactors.accSelection(boundary).browseAccounts();
		
		return result.size() == 0 ? null : result.get(0);
	}
	
	private List<AccountData> buildCandidateAccList(List<AccountData> theAccList)
	{
		ArrayList<AccountData> candidateAccs = new ArrayList<AccountData>();
		
		candidateAccs.addAll(theAccList);
		candidateAccs.add(OTHER_ACC_PLACEHOLDER);
		
		return candidateAccs;
	}

	// -- data --
	
	@SuppressWarnings("deprecation")
	private void filter_execute(Date fromDate, Date toDate)
	{
		Date lowerBound = fromDate != null ? fromDate : new Date(Long.MIN_VALUE);

		Date upperBound = toDate;
		if (upperBound != null)
		{
			upperBound = (Date) toDate.clone();
			upperBound.setHours(23);
			upperBound.setMinutes(59);
			upperBound.setSeconds(59);
		}
		else
		{
			upperBound = new Date(Long.MAX_VALUE);
		}

		_interactors.txBrowse(txBrowseResponse).filterTxList(lowerBound, upperBound);;
	}

	// -- view --
	
	private void selectSource_execute(TxView txView)
	{
		try
		{
			AccountData selectedAccount = selectAccountFromTree();

			if (selectedAccount != null)
			{
				_interactors.accSelection(accSelectionResponse).setSourceAccount(selectedAccount);
			}
		}
		catch (Exception e)
		{
			txView.handleException(e);
		}
	}

	private void selectTarget_execute(TxView txView)
	{
		try
		{
			AccountData selectedAccount = selectAccountFromTree();
			
			if (selectedAccount != null)
			{
				_interactors.accSelection(accSelectionResponse).setTargetHierarchy(selectedAccount);
			}
		}
		catch (Exception e)
		{
			txView.handleException(e);
		}
	}

	private void loadCsv_execute(TxView txView)
	{
		try
		{
			String lastDir = _config.getLastCsvDirectory();
			
			if(lastDir == null || lastDir.isEmpty())
			{
				lastDir = System.getProperty("user.home");
			}
			
			final String fileName = txView.promptForFile(lastDir);
			
			if (fileName != null)
			{					
				_interactors.txBrowse(txBrowseResponse).fetchTransactions(fileName);				
			}
		}
		catch (Exception e)
		{
			txView.handleException(e);
		}
	}

	private  void loadGnc_execute(TxView txView)
	{
		try
		{
			String lastGncDirectory = _config.getLastGncDirectory();
			
			if(lastGncDirectory == null || lastGncDirectory.isEmpty())
			{
				lastGncDirectory = System.getProperty("user.home");
			}
			
			String fileName = txView.promptForFile(lastGncDirectory);
			
			if (fileName != null)
			{
				_interactors.accFileLoad(accFileLoadReponse).openGncFile(fileName);
			}
		}
		catch (Exception e)
		{
			txView.handleException(e);
		}
	}

	// -- data + view --
	
	private void saveGnc_execute(String fileName, TxView txView)
	{
		try
		{
			_interactors.txImport().saveTxTo(txView.getTxTableModel().getTransactions(), fileName);
		}
		catch (Exception e)
		{
			txView.handleException(e);
		}
	}

	private void createAcc_execute(String fileNameToSave, TxView txView)
	{
		if (fileNameToSave == null || fileNameToSave.trim().isEmpty())
		{
			txView.displayErrorMessage("GNC file must be opened first!");
			return;
		}
	
		try
		{
			_interactors.accSelection(accSelectionResponse).browseAccounts();
			
			AccountData selectedAccount = accSelectionResponse.selectedAccount;
			
			if (selectedAccount != null)
			{
				_interactors.accHierarchyCreation().createNewAccountHierarchy(
						selectedAccount, accSelectionResponse.params.rootAccName, accSelectionResponse.params.month,
						_config.getMonthlyAccounts(), fileNameToSave);
				
			}
		}
		catch (Exception e)
		{
			txView.handleException(e);
		}
	}

	private AccountData accSelection_execute(AccountData newAcc, AccountData originalAcc, TxView txView)
	{
		if (!newAcc.equals(OTHER_ACC_PLACEHOLDER))
		{
			return newAcc;
		}

		try
		{
			AccountData selectedAcc = selectAccountFromTree();

			if (selectedAcc != null)
			{
				return selectedAcc;
			}
			else
			{
				return originalAcc;
			}
		}
		catch (Exception e)
		{
			txView.handleException(e);
			return originalAcc;
		}
	}

}
