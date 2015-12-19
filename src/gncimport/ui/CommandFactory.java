package gncimport.ui;

import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.InteractorFactory;
import gncimport.interactors.TxBrowseInteractor;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory
{		
	private InteractorFactory _interactors;
	private TxView _view;
	private UIConfig _config;
	
	private Map<String, Command<? extends Event>> _commands = new HashMap<String, Command<? extends Event>>();
	
		
	public CommandFactory(TxView view, UIConfig config, InteractorFactory interactors)
	{
		_interactors = interactors;
		_view = view;
		_config = config;
		
		TxBrowseInteractor.OutPort txBrowsePresenter = new TxBrowsePresenter(view, config); 
		AccFileLoadInteractor.OutPort accFileLoadPresenter = new AccFileLoadPresenter(view, config);
		AccSelectionInteractor.OutPort accSelectionPresenter = new AccSelectionPresenter(view);
	
		registerEvent(NoArgsEvent.LoadCsvEvent, new LoadCsvCommand(_view, _config, _interactors.txBrowse(txBrowsePresenter)));
		registerEvent(NoArgsEvent.LoadGncEvent, new LoadGncCommand(_view, _config, _interactors.accFileLoad(accFileLoadPresenter)));
		registerEvent(NoArgsEvent.SelectSourceAccEvent, new SelectSourceAccCommand(_view, _interactors.accSelection(accSelectionPresenter)));
		registerEvent(NoArgsEvent.SelectTargetAccEvent, new SelectTargetAccCommand(_view, _interactors.accSelection(accSelectionPresenter)));

		registerEvent(FilterTxListEvent.class.getName(), new FilterTxListCommand(_interactors.txBrowse(txBrowsePresenter)));
		registerEvent(SaveGncEvent.class.getName(), new SaveGncCommand(_view, _interactors.txImport()));
		registerEvent(SelectExpenseAccEvent.class.getName(), new SelectExpenseAccCommand(_view, _interactors.accSelection(accSelectionPresenter)));
		registerEvent(CreateAccHierarchyEvent.class.getName(), new CreateAccHierarchyCommand(_view, _config, _interactors.accSelection(accSelectionPresenter)));
	}

	public <T extends Event> void registerEvent(String eventId, Command<T> command)
	{
		_commands.put(eventId, command);
	}

	public <T extends Event> void triggerWithArgs(T args)
	{		
		@SuppressWarnings("unchecked")
		Command<T> cmd = (Command<T>) _commands.get(args.getClass().getName());
		cmd.execute(args);
	}

	public void triggerWithoutArgs(String eventId)
	{		
		@SuppressWarnings("unchecked")
		Command<NoArgsEvent> cmd = (Command<NoArgsEvent>) _commands.get(eventId);
		cmd.execute(new NoArgsEvent());
	}
}
