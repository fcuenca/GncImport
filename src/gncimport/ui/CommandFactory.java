package gncimport.ui;

import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.InteractorFactory;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.models.AccountData;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory
{		
	private InteractorFactory _interactors;
	private TxView _view;
	private UIConfig _config;
	
	private Map<String, Command<? extends Event>> _commands = new HashMap<String, Command<? extends Event>>();
	
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
	
		registerEvent(FilterTxListEvent.class.getName(), new FilterTxListCommand(_interactors.txBrowse(_txBrowsePresenter)));
		registerEvent(NoArgsEvent.LoadCsvEvent, new LoadCsvCommand(_view, _config, _interactors.txBrowse(_txBrowsePresenter)));
		registerEvent(NoArgsEvent.LoadGncEvent, new LoadGncCommand(_view, _config, _interactors.accFileLoad(_accFileLoadPresenter)));
		registerEvent(SaveGncEvent.class.getName(), new SaveGncCommand(_view, _interactors.txImport()));
	}

	public <T extends Event> void registerEvent(String eventClass, Command<T> command)
	{
		_commands.put(eventClass, command);
	}

	public <T extends Event> void trigger(T args)
	{		
		@SuppressWarnings("unchecked")
		Command<T> cmd = (Command<T>) _commands.get(args.getClass().getName());
		cmd.execute(args);
	}

	public void trigger(String eventId)
	{		
		@SuppressWarnings("unchecked")
		Command<NoArgsEvent> cmd = (Command<NoArgsEvent>) _commands.get(eventId);
		cmd.execute(new NoArgsEvent());
	}

	public void loadGnc()
	{
//		LoadGncCommand cmd = new LoadGncCommand(_view, _config, _interactors.accFileLoad(_accFileLoadPresenter));
//		cmd.execute(new NoArgsEvent());
	}

	public void selectSourceAcc()
	{
		SelectSourceAccCommand cmd = new SelectSourceAccCommand(_view, _interactors.accSelection(_accSelectionPresenter));
		cmd.execute();
	}

	public void selectTargetAcc()
	{
		SelectTargetAccCommand cmd = new SelectTargetAccCommand(_view, _interactors.accSelection(_accSelectionPresenter));
		cmd.execute();
	}

	public void selectExpenseAcc(AccountData newAcc, AccountData originalAcc)
	{
		SelectExpenseAccCommand cmd = new SelectExpenseAccCommand(newAcc, originalAcc, _view, _interactors.accSelection(_accSelectionPresenter));
		cmd.execute();
	}

	public void createAccHierarchy(String fileNameToSave)
	{
		CreateAccHierarchyCommand cmd = new CreateAccHierarchyCommand(fileNameToSave, _view, _config, _interactors.accSelection(_accSelectionPresenter));
		cmd.execute();
	}

}
