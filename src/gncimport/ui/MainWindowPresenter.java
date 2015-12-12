package gncimport.ui;

import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccFileLoadInteractor.OutPort;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.InteractorFactory;
import gncimport.interactors.TxClassificationInteractor;
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
	
	AccSelectionInteractor.OutPort doNothingAccSelectionResponse = new AccSelectionInteractor.OutPort () 
	{
		@Override
		public void accept(List<AccountData> accounts)
		{
			throw new ProgrammerError("nothing to do for now");
		}
	};				
	
	TxBrowseInteractor.OutPort txBrowseResponse = new TxBrowseInteractor.OutPort() 
	{
		@Override
		public void accept(List<TxData> txList)
		{
			//TODO: pass the account list to this call back -> remove the need to call
			// an interactor from a response object!!
			final List<AccountData> accList = buildTargetAccountList();
			do_accept(txList, accList);
		}

		private void do_accept(List<TxData> txList, final List<AccountData> accList)
		{
			_view.displayTxData(new TxTableModel(txList), accList);
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

	@Override
	public void onReadFromCsvFile()
	{
		loadCsv_execute(_view, txBrowseResponse);
	}
	
	@Override
	public void onFilterTxList(Date fromDate, Date toDate)
	{
		filter_execute(fromDate, toDate, txBrowseResponse);				
	}
	
	@Override
	public void onSaveToGncFile(String fileName)
	{
		saveGnc_execute(fileName, _view);
	}

	@Override
	public void onLoadGncFile()
	{
		loadGnc_execute(_config, _view, accFileLoadReponse);
	}
	
	@Override
	public void onSelectSourceAccount()
	{
		selectSource_execute(_view, doNothingAccSelectionResponse);
	}

	@Override
	public void onSelectTargetHierarchy()
	{
		selectTarget_execute(_view, doNothingAccSelectionResponse);
	}

	@Override
	public void onCreateNewAccHierarchy(String fileNameToSave)
	{	
		createAcc_execute(fileNameToSave, _view, _config);
	}

	@Override
	public AccountData onTargetAccountSelected(AccountData newAcc, AccountData originalAcc)
	{
		return accSelection_execute(newAcc, originalAcc, _view);
	}

	private List<AccountData> buildTargetAccountList()
	{
		final ArrayList<AccountData> candidates = new ArrayList<AccountData>();

		TxClassificationInteractor.OutPort boundary = new TxClassificationInteractor.OutPort()
		{
			@Override
			public void accept(List<AccountData> accounts)
			{
				candidates.addAll(accounts);
				candidates.add(OTHER_ACC_PLACEHOLDER);
			}
		};
		
		_interactors.txClassification(boundary).getCandidateTargetAccounts();;
		
		return candidates;
	}

	private AccountData selectAccountFromTree(TxView txView)
	{
		DefaultMutableTreeNode accountRoot = getAccountTree();
		DefaultMutableTreeNode selectedNode = txView.promptForAccount(accountRoot);

		if (selectedNode != null)
		{
			AccountData selectedAccount = (AccountData) selectedNode.getUserObject();
			return selectedAccount;
		}

		return null;
	}

	private DefaultMutableTreeNode getAccountTree()
	{
		final AccountTreeBuilder builder = new AccountTreeBuilder();
		
		AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort()
		{
			@Override
			public void accept(List<AccountData> accounts)
			{
				for (AccountData account : accounts)
				{
					builder.addNodeFor(account);
				}
			}
		};

		_interactors.accSelection(boundary).getAccounts();;

		return builder.getRoot();
	}

	@SuppressWarnings("deprecation")
	private void filter_execute(Date fromDate, Date toDate, TxBrowseInteractor.OutPort outPort)
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

		_interactors.txBrowse(outPort).filterTxList(lowerBound, upperBound);;
	}

	private void loadCsv_execute(TxView txView, TxBrowseInteractor.OutPort outPort)
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
				_interactors.txBrowse(outPort).fetchTransactions(fileName);				
			}
		}
		catch (Exception e)
		{
			txView.handleException(e);
		}
	}

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

	private  void loadGnc_execute(UIConfig config, TxView txView, OutPort outPort)
	{
		try
		{
			String lastGncDirectory = config.getLastGncDirectory();
			
			if(lastGncDirectory == null || lastGncDirectory.isEmpty())
			{
				lastGncDirectory = System.getProperty("user.home");
			}
			
			String fileName = txView.promptForFile(lastGncDirectory);
			
			if (fileName != null)
			{
				_interactors.accFileLoad(outPort).openGncFile(fileName);
			}
		}
		catch (Exception e)
		{
			txView.handleException(e);
		}
	}

	private void selectSource_execute(TxView txView, AccSelectionInteractor.OutPort outPort)
	{
		try
		{
			AccountData selectedAccount = selectAccountFromTree(_view);

			if (selectedAccount != null)
			{
				_interactors.accSelection(outPort).setSourceAccount(selectedAccount);;
				
				txView.displaySourceAccount(selectedAccount.getName());
			}
		}
		catch (Exception e)
		{
			txView.handleException(e);
		}
	}

	private void selectTarget_execute(TxView txView, AccSelectionInteractor.OutPort outPort)
	{
		try
		{
			AccountData selectedAccount = selectAccountFromTree(txView);
			if (selectedAccount != null)
			{
				_interactors.accSelection(outPort).setTargetHierarchy(selectedAccount);

				txView.displayTargetHierarchy(selectedAccount.getName());
				//TODO: do this call from the setTargetHierarchy callback (which now does nothing)
				txView.updateCandidateTargetAccountList(buildTargetAccountList());
			}
		}
		catch (Exception e)
		{
			txView.handleException(e);
		}
	}

	private void createAcc_execute(String fileNameToSave, TxView txView, UIConfig config)
	{
		if (fileNameToSave == null || fileNameToSave.trim().isEmpty())
		{
			txView.displayErrorMessage("GNC file must be opened first!");
			return;
		}
	
		try
		{
			DefaultMutableTreeNode accountRoot = getAccountTree();
			NewHierarchyParams params = txView.promptForNewHierarchy(accountRoot);
			
			if (params != null)
			{
				if (params.parentNode == null || params.rootAccName == null || params.rootAccName.trim().isEmpty())
				{
					throw new ProgrammerError("Invalid values for new Hierarchy came through!!");
				}
				
				AccountData selectedAccount = (AccountData) params.parentNode.getUserObject();
				
				_interactors.accHierarchyCreation().createNewAccountHierarchy(
						selectedAccount, params.rootAccName, params.month,
						config.getMonthlyAccounts(), fileNameToSave);
				
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
			final AccountData selectedAcc = selectAccountFromTree(txView);
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
