package gncimport.ui;

import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.AccSelectionInteractor.NewHierarchyOpts;
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
	private final CommandFactory _commands;

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

		public SelectSourceAccCommand selectSourceAcc()
		{
			return new SelectSourceAccCommand(_view, _interactors.accSelection(accSelectionResponse));
		}

		public SelectTargetAccCommand selectTargetAcc()
		{
			return new SelectTargetAccCommand(_view, _interactors.accSelection(accSelectionResponse));
		}

		public SelectExpenseAccCommand selectExpenseAcc(AccountData newAcc, AccountData originalAcc)
		{
			return new SelectExpenseAccCommand(newAcc, originalAcc, _view, _interactors.accSelection(accSelectionResponse));
		}

		public CreateAccHierarchyCommand createAccHierarchy(String fileNameToSave)
		{
			return new CreateAccHierarchyCommand(fileNameToSave, _view, _config, _interactors.accSelection(accSelectionResponse));
		}
	}
	
	class CreateAccHierarchyCommand
	{
		private String _fileNameToSave;
		private TxView _theView;
		private AccSelectionInteractor _theInteractor;
		private UIConfig _theConfig;

		public CreateAccHierarchyCommand(String fileNameToSave, TxView view, UIConfig config, AccSelectionInteractor interactor)
		{
			this._fileNameToSave = fileNameToSave;
			this._theView = view;
			this._theConfig = config;
			this._theInteractor = interactor;
		}

		public void execute()
		{
			if (_fileNameToSave == null || _fileNameToSave.trim().isEmpty())
			{
				_theView.displayErrorMessage("GNC file must be opened first!");
				return;
			}
		
			try
			{				
				 _theInteractor.createNewAccountHierarchy(_theConfig.getMonthlyAccounts(), _fileNameToSave);
			}
			catch (Exception e)
			{
				_theView.handleException(e);
			}
		}
		
	}
	
	class SelectExpenseAccCommand
	{
		private AccountData _newAcc;
		private AccountData _originalAcc;
		private TxView _theView;
		private AccSelectionInteractor _theInteractor;

		public SelectExpenseAccCommand(AccountData newAcc, AccountData originalAcc, TxView view, AccSelectionInteractor interactor)
		{
			this._newAcc = newAcc;
			this._originalAcc = originalAcc;
			this._theView = view;
			this._theInteractor = interactor;
		}

		public AccountData execute()
		{
			if (!_newAcc.equals(OTHER_ACC_PLACEHOLDER))
			{
				return _newAcc;
			}

			try
			{				
				AccountData selectedAcc = _theInteractor.browseAccounts();

				if (selectedAcc != null)
				{
					return selectedAcc;
				}
				else
				{
					return _originalAcc;
				}
			}
			catch (Exception e)
			{
				_theView.handleException(e);
				return _originalAcc;
			}
		}
	}
	
	class SelectTargetAccCommand
	{
		private TxView _theView;
		private AccSelectionInteractor _theInteractor;

		public SelectTargetAccCommand(TxView view, AccSelectionInteractor interactor)
		{
			this._theView = view;
			this._theInteractor = interactor;
		}

		public void execute()
		{
			try
			{
				_theInteractor.selectTargetAccount();				
			}
			catch (Exception e)
			{
				_theView.handleException(e);
			}			
		}
	}
	
	class SelectSourceAccCommand
	{
		private TxView _theView;
		private AccSelectionInteractor _theInteractor;

		public SelectSourceAccCommand(TxView view, AccSelectionInteractor interactor)
		{
			this._theView = view;
			this._theInteractor = interactor;
		}

		public void execute()
		{
			try
			{
				_theInteractor.selectSourceAccount();
			}
			catch (Exception e)
			{
				_theView.handleException(e);
			}			
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
	
	
	public MainWindowPresenter(TxImportModel model, TxView view, UIConfig config)
	{
		this._interactors = new InteractorFactory(model);

		this._view = view;
		this._config = config;
		
		_commands = new CommandFactory(_view, _config);
	}
	
			
	AccSelectionInteractor.OutPort accSelectionResponse = new AccSelectionInteractor.OutPort() 
	{
		@Override
		public AccountData selectAccount(List<AccountData> accounts)
		{
			DefaultMutableTreeNode selectedNode = _view.promptForAccount(getRootNode(accounts));
			
			if (selectedNode != null)
			{
				return (AccountData) selectedNode.getUserObject();
			}
						
			return null;
		}


		@Override
		public NewHierarchyOpts promptForNewHierarchy(List<AccountData> accounts)
		{
			NewHierarchyParams params = _view.promptForNewHierarchy(getRootNode(accounts));
			
			if (params != null)
			{
				if (params.parentNode == null || params.rootAccName == null || params.rootAccName.trim().isEmpty())
				{
					throw new ProgrammerError("Invalid values for new Hierarchy came through!!");
				}
								
				return new AccSelectionInteractor.NewHierarchyOpts((AccountData) params.parentNode.getUserObject(), params.rootAccName, params.month);
			}
			
			return null;
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

		private DefaultMutableTreeNode getRootNode(List<AccountData> accounts)
		{
			AccountTreeBuilder builder = new AccountTreeBuilder();
			
			for (AccountData account : accounts)
			{
				builder.addNodeFor(account);
			}
			
			return builder.getRoot();
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
		_commands.selectSourceAcc().execute();
	}

	@Override
	public void onSelectTargetHierarchy()
	{
		_commands.selectTargetAcc().execute();
	}

	@Override
	public AccountData onTargetAccountSelected(AccountData newAcc, AccountData originalAcc)
	{
		return _commands.selectExpenseAcc(newAcc, originalAcc).execute();
	}

	@Override
	public void onCreateNewAccHierarchy(String fileNameToSave)
	{	
		_commands.createAccHierarchy(fileNameToSave).execute();
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
}
