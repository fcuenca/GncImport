package gncimport.ui;

import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.InteractorFactory;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.interactors.TxImportInteractor;
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

	class CommandFactory
	{		
		private TxView _theView;
		private UIConfig _theConfig;

		public CommandFactory(TxView view, UIConfig config)
		{
			this._theView = view;
			this._theConfig = config;
		}
		
		public LoadCsvCommand loadCsv()
		{
			return new LoadCsvCommand(_theView, _theConfig, _interactors.txBrowse(txBrowseResponse));
		}

		public LoadGncCommand loadGnc()
		{
			return new LoadGncCommand(_view, _config, _interactors.accFileLoad(accFileLoadReponse));
		}

		public FilterTxListCommand filterTxList(Date fromDate, Date toDate)
		{
			return new FilterTxListCommand(fromDate, toDate, _interactors.txBrowse(txBrowseResponse));
		}

		public SaveGncCommand saveGnc(String fileName)
		{
			return new SaveGncCommand(fileName, _theView, _interactors.txImport());
		}
	}
	
	class LoadGncCommand
	{

		private TxView _theView;
		private UIConfig _theConfig;
		private AccFileLoadInteractor _theInteractor;

		public LoadGncCommand(TxView view, UIConfig config, AccFileLoadInteractor interactor)
		{
			this._theView = view;
			this._theConfig = config;
			this._theInteractor = interactor;
		}

		public void execute()
		{
			try
			{
				String lastGncDirectory = _theConfig.getLastGncDirectory();
				
				if(lastGncDirectory == null || lastGncDirectory.isEmpty())
				{
					lastGncDirectory = System.getProperty("user.home");
				}
				
				String fileName = _theView.promptForFile(lastGncDirectory);
				
				if (fileName != null)
				{
					_theInteractor.openGncFile(fileName);
				}
			}
			catch (Exception e)
			{
				_theView.handleException(e);
			}			
		}
		
	}
	
	class SaveGncCommand
	{

		private String _fileName;
		private TxView _theView;
		private TxImportInteractor _theInteractor;

		public SaveGncCommand(String fileName, TxView view, TxImportInteractor interactor)
		{
			this._fileName = fileName;
			this._theView = view;
			this._theInteractor = interactor;
		}

		public void execute()
		{
			try
			{
				_theInteractor.saveTxTo(_theView.getTxTableModel().getTransactions(), _fileName);
			}
			catch (Exception e)
			{
				_theView.handleException(e);
			}
		}		
	}
	
	class LoadCsvCommand
	{		
		private TxView _theView;
		private UIConfig _theConfig;
		private TxBrowseInteractor _theInteractor;

		public LoadCsvCommand(TxView view, UIConfig config, TxBrowseInteractor interactor)
		{
			_theView = view;
			_theConfig = config;
			_theInteractor = interactor;
		}

		public void execute()
		{
			try
			{
				String lastDir = _theConfig.getLastCsvDirectory();
				
				if(lastDir == null || lastDir.isEmpty())
				{
					lastDir = System.getProperty("user.home");
				}
				
				final String fileName = _theView.promptForFile(lastDir);
				
				if (fileName != null)
				{					
					_theInteractor.fetchTransactions(fileName);				
				}
			}
			catch (Exception e)
			{
				_theView.handleException(e);
			}
		}
	}

	class FilterTxListCommand
	{
		private Date _fromDate;
		private Date _toDate;
		private TxBrowseInteractor _theInteractor;

		public FilterTxListCommand(Date fromDate, Date toDate, TxBrowseInteractor interactor)
		{
			_fromDate = fromDate;
			_toDate = toDate;
			_theInteractor = interactor;
		}

		@SuppressWarnings("deprecation")
		public void execute()
		{	
			Date lowerBound = _fromDate != null ? _fromDate : new Date(Long.MIN_VALUE);

			Date upperBound = _toDate;
			if (upperBound != null)
			{
				upperBound = (Date) _toDate.clone();
				upperBound.setHours(23);
				upperBound.setMinutes(59);
				upperBound.setSeconds(59);
			}
			else
			{
				upperBound = new Date(Long.MAX_VALUE);
			}

			_theInteractor.filterTxList(lowerBound, upperBound);;
		}
		
	}
	
	CommandFactory _commands;
	
	public MainWindowPresenter(TxImportModel model, TxView view, UIConfig config)
	{
		this._interactors = new InteractorFactory(model);

		this._view = view;
		this._config = config;
		
		_commands = new CommandFactory(_view, _config);
	}
	
	abstract class AccSelectionInteractorOutPort implements AccSelectionInteractor.OutPort
	{
		public AccountData selectedAccount;
		
		@Override
		public void accept(List<AccountData> accounts)
		{			
			AccountTreeBuilder builder = new AccountTreeBuilder();
			
			for (AccountData account : accounts)
			{
				builder.addNodeFor(account);
			}
			
			DefaultMutableTreeNode accountRoot = builder.getRoot();		
			
			selectedAccount = selectAccountFromTree(accountRoot);
		}

		abstract protected AccountData selectAccountFromTree(DefaultMutableTreeNode accountRoot);
		
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
	
	class NewHierarchyAccSelectionOutPort extends AccSelectionInteractorOutPort
	{
		public NewHierarchyParams params; 

		@Override
		protected AccountData selectAccountFromTree(DefaultMutableTreeNode accountRoot)
		{
			params = _view.promptForNewHierarchy(accountRoot);
			
			if (params != null)
			{
				if (params.parentNode == null || params.rootAccName == null || params.rootAccName.trim().isEmpty())
				{
					throw new ProgrammerError("Invalid values for new Hierarchy came through!!");
				}
				
				return (AccountData) params.parentNode.getUserObject();				
			}
			
			return null;
		}		
	}
	
	NewHierarchyAccSelectionOutPort newHierarchyAccSelectionResponse = new NewHierarchyAccSelectionOutPort();
	
	AccSelectionInteractorOutPort accSelectionResponse = new AccSelectionInteractorOutPort() {

		@Override
		protected AccountData selectAccountFromTree(DefaultMutableTreeNode accountRoot)
		{
			DefaultMutableTreeNode selectedNode = _view.promptForAccount(accountRoot);

			if (selectedNode != null)
			{
				return (AccountData) selectedNode.getUserObject();
			}
						
			return null;
		}
	};	
	
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
		_commands.loadCsv().execute();
	}
	
	@Override
	public void onFilterTxList(Date fromDate, Date toDate)
	{
		_commands.filterTxList(fromDate, toDate).execute();				
	}
	
	@Override
	public void onSaveToGncFile(String fileName)
	{
		_commands.saveGnc(fileName).execute();
	}

	@Override
	public void onLoadGncFile()
	{
		_commands.loadGnc().execute();
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
	
	//TODO: remove dependency from two different outPorts to this function (?)
	private List<AccountData> buildCandidateAccList(List<AccountData> theAccList)
	{
		ArrayList<AccountData> candidateAccs = new ArrayList<AccountData>();
		
		candidateAccs.addAll(theAccList);
		candidateAccs.add(OTHER_ACC_PLACEHOLDER);
		
		return candidateAccs;
	}

	// -- view --
	
	private void selectSource_execute(TxView txView)
	{		
		try
		{
			_interactors.accSelection(accSelectionResponse).browseAccounts();
			
			AccountData selectedAccount = accSelectionResponse.selectedAccount;

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
			_interactors.accSelection(accSelectionResponse).browseAccounts();
			AccountData selectedAccount = accSelectionResponse.selectedAccount;
			
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
	
	

	// -- data + view --
	
	private void createAcc_execute(String fileNameToSave, TxView txView)
	{
		if (fileNameToSave == null || fileNameToSave.trim().isEmpty())
		{
			txView.displayErrorMessage("GNC file must be opened first!");
			return;
		}
	
		try
		{
			_interactors.accSelection(newHierarchyAccSelectionResponse).browseAccounts();
			
			AccountData selectedAccount = newHierarchyAccSelectionResponse.selectedAccount;
			
			if (selectedAccount != null)
			{
				_interactors.accHierarchyCreation().createNewAccountHierarchy(
						selectedAccount, 
						newHierarchyAccSelectionResponse.params.rootAccName, 
						newHierarchyAccSelectionResponse.params.month,
						_config.getMonthlyAccounts(), 
						fileNameToSave);
				
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
			_interactors.accSelection(accSelectionResponse).browseAccounts();
			AccountData selectedAcc = accSelectionResponse.selectedAccount;

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
