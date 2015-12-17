package gncimport.ui;

import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.InteractorFactory;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.models.AccountData;

import java.util.Date;

public class CommandFactory
{		
	private InteractorFactory _interactors;
	private TxView _view;
	private UIConfig _config;
	
	private TxBrowseInteractor.OutPort _txBrowsePresenter; 
	private AccFileLoadInteractor.OutPort _accFileLoadPresenter;
	private AccSelectionInteractor.OutPort _accSelectionPresenter;
	
	public CommandFactory(TxView view, UIConfig config, InteractorFactory interactors)
	{
		_interactors = interactors;
		_view = view;
		_config = config;
		
		_txBrowsePresenter = new TxBrowsePresenter(view, config); 
		_accFileLoadPresenter = new AccFileLoadPresenter(view, config);
		_accSelectionPresenter = new AccSelectionPresenter(view);
	}
	
	public LoadCsvCommand loadCsv()
	{
		return new LoadCsvCommand(_view, _config, _interactors.txBrowse(_txBrowsePresenter));
	}

	public LoadGncCommand loadGnc()
	{
		return new LoadGncCommand(_view, _config, _interactors.accFileLoad(_accFileLoadPresenter));
	}

	public FilterTxListCommand filterTxList(Date fromDate, Date toDate)
	{
		return new FilterTxListCommand(fromDate, toDate, _interactors.txBrowse(_txBrowsePresenter));
	}

	public SaveGncCommand saveGnc(String fileName)
	{
		return new SaveGncCommand(fileName, _view, _interactors.txImport());
	}

	public SelectSourceAccCommand selectSourceAcc()
	{
		return new SelectSourceAccCommand(_view, _interactors.accSelection(_accSelectionPresenter));
	}

	public SelectTargetAccCommand selectTargetAcc()
	{
		return new SelectTargetAccCommand(_view, _interactors.accSelection(_accSelectionPresenter));
	}

	public SelectExpenseAccCommand selectExpenseAcc(AccountData newAcc, AccountData originalAcc)
	{
		return new SelectExpenseAccCommand(newAcc, originalAcc, _view, _interactors.accSelection(_accSelectionPresenter));
	}

	public CreateAccHierarchyCommand createAccHierarchy(String fileNameToSave)
	{
		return new CreateAccHierarchyCommand(fileNameToSave, _view, _config, _interactors.accSelection(_accSelectionPresenter));
	}
}
