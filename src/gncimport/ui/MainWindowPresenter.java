package gncimport.ui;

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

	@Override
	public void onReadFromCsvFile()
	{
		try
		{
			String lastDir = _config.getLastCsvDirectory();
			
			if(lastDir == null || lastDir.isEmpty())
			{
				lastDir = System.getProperty("user.home");
			}
			
			final String fileName = _view.promptForFile(lastDir);
			
			if (fileName != null)
			{
				TxBrowseInteractor.OutPort boundary = new TxBrowseInteractor.OutPort() 
				{
					@Override
					public void accept(List<TxData> newTransactionData)
					{
						_view.displayTxData(new TxTableModel(newTransactionData), buildTargetAccountList());
						_view.displayTxCount(newTransactionData.size());
						_config.setLastCsvDirectory(new File(fileName).getParent());
						_view.updateCsvFileLabel(fileName);
					}
				};
					
				_interactors.txFileLoad(boundary).fetchTransactions(fileName);				
			}
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
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

	@Override
	public void onSaveToGncFile(String fileName)
	{
		try
		{
			_interactors.txImport().saveTxTo(_view.getTxTableModel().getTransactions(), fileName);
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	@Override
	public void onLoadGncFile()
	{
		try
		{
			String lastGncDirectory = _config.getLastGncDirectory();
			
			if(lastGncDirectory == null || lastGncDirectory.isEmpty())
			{
				lastGncDirectory = System.getProperty("user.home");
			}
			
			String fileName = _view.promptForFile(lastGncDirectory);
			
			if (fileName != null)
			{
				_interactors.accFileLoad().openGncFile(fileName);

				_view.updateGncFileLabel(fileName);
				_config.setLastGncDirectory(new File(fileName).getParent());
			}
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	@Override
	public void onSelectSourceAccount()
	{
		try
		{
			AccountData selectedAccount = selectAccountFromTree();

			if (selectedAccount != null)
			{
				AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort () 
				{
					@Override
					public void accept(List<AccountData> accounts)
					{
						throw new ProgrammerError("nothing to do for now");
					}
				};
				
				_interactors.accSelection(boundary).setSourceAccount(selectedAccount);;
				
				_view.displaySourceAccount(selectedAccount.getName());
			}
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	@Override
	public void onSelectTargetHierarchy()
	{
		try
		{
			AccountData selectedAccount = selectAccountFromTree();
			if (selectedAccount != null)
			{
				AccSelectionInteractor.OutPort boundary = new AccSelectionInteractor.OutPort () 
				{
					@Override
					public void accept(List<AccountData> accounts)
					{
						throw new ProgrammerError("nothing to do for now");
					}
				};				

				_interactors.accSelection(boundary).setTargetHierarchy(selectedAccount);

				_view.displayTargetHierarchy(selectedAccount.getName());
				_view.updateCandidateTargetAccountList(buildTargetAccountList());
			}
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	@Override
	public void onCreateNewAccHierarchy(String fileNameToSave)
	{	
		if (fileNameToSave == null || fileNameToSave.trim().isEmpty())
		{
			_view.displayErrorMessage("GNC file must be opened first!");
			return;
		}
	
		try
		{
			DefaultMutableTreeNode accountRoot = getAccountTree();
			NewHierarchyParams params = _view.promptForNewHierarchy(accountRoot);
			
			if (params != null)
			{
				if (params.parentNode == null || params.rootAccName == null || params.rootAccName.trim().isEmpty())
				{
					throw new ProgrammerError("Invalid values for new Hierarchy came through!!");
				}
				
				AccountData selectedAccount = (AccountData) params.parentNode.getUserObject();
				
				_interactors.accHierarchyCreation().createNewAccountHierarchy(
						selectedAccount, params.rootAccName, params.month,
						_config.getMonthlyAccounts(), fileNameToSave);
				
			}
		}
		catch (Exception e)
		{
			_view.handleException(e);
		}
	}

	@Override
	public AccountData onTargetAccountSelected(AccountData newAcc, AccountData originalAcc)
	{
		if (!newAcc.equals(OTHER_ACC_PLACEHOLDER))
		{
			return newAcc;
		}

		try
		{
			final AccountData selectedAcc = selectAccountFromTree();
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
			_view.handleException(e);
			return originalAcc;
		}
	}

	private AccountData selectAccountFromTree()
	{
		DefaultMutableTreeNode accountRoot = getAccountTree();
		DefaultMutableTreeNode selectedNode = _view.promptForAccount(accountRoot);

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
	@Override
	public void onFilterTxList(Date fromDate, Date toDate)
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

		TxBrowseInteractor.OutPort boundary = new TxBrowseInteractor.OutPort() 
		{
			@Override
			public void accept(List<TxData> txList)
			{
				_view.displayTxData(new TxTableModel(txList), buildTargetAccountList());
				_view.displayTxCount(txList.size());
			}
		};
			
		_interactors.txFileLoad(boundary).filterTxList(lowerBound, upperBound);;				
	}

}
