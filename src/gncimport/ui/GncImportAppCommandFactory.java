package gncimport.ui;

import gncimport.interactors.AccFileLoadInteractor;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.InteractorFactory;
import gncimport.interactors.TxBrowseInteractor;

import java.util.HashMap;
import java.util.Map;

public class GncImportAppCommandFactory implements CommandFactory
{		
	private InteractorFactory _interactors;
	private UIConfig _config;
	
	private Map<String, Command<? extends Event>> _commands = new HashMap<String, Command<? extends Event>>();
	
	//TODO: remove this constructor once the presenter is removed
	public GncImportAppCommandFactory(TxView view, UIConfig config, InteractorFactory interactors)
	{
		_interactors = interactors;
		_config = config;
		attachToView(view);
	}

	public GncImportAppCommandFactory(UIConfig config, InteractorFactory interactors)
	{
		_interactors = interactors;
		_config = config;
	}
	
	@Override
	public <T extends Event> void registerEvent(String eventId, Command<T> command)
	{
		_commands.put(eventId, command);
	}

	@Override
	public <T extends Event> void triggerWithArgs(T args)
	{		
		@SuppressWarnings("unchecked")
		Command<T> cmd = (Command<T>) _commands.get(args.getClass().getName());
		cmd.execute(args);
	}

	@Override
	public void triggerWithoutArgs(String eventId)
	{		
		@SuppressWarnings("unchecked")
		Command<NoArgsEvent> cmd = (Command<NoArgsEvent>) _commands.get(eventId);
		cmd.execute(new NoArgsEvent());
	}

	@Override
	public void attachToView(TxView view)
	{
		TxBrowseInteractor.OutPort txBrowsePresenter = new TxBrowsePresenter(view, _config); 
		AccFileLoadInteractor.OutPort accFileLoadPresenter = new AccFileLoadPresenter(view, _config);
		AccSelectionInteractor.OutPort accSelectionPresenter = new AccSelectionPresenter(view);
	
		registerEvent(NoArgsEvent.LoadCsvEvent, new LoadCsvCommand(view, _config, _interactors.txBrowse(txBrowsePresenter)));
		registerEvent(NoArgsEvent.LoadGncEvent, new LoadGncCommand(view, _config, _interactors.accFileLoad(accFileLoadPresenter)));
		registerEvent(NoArgsEvent.SelectSourceAccEvent, new SelectSourceAccCommand(view, _interactors.accSelection(accSelectionPresenter)));
		registerEvent(NoArgsEvent.SelectTargetAccEvent, new SelectTargetAccCommand(view, _interactors.accSelection(accSelectionPresenter)));

		registerEvent(FilterTxListEvent.class.getName(), new FilterTxListCommand(view, _interactors.txBrowse(txBrowsePresenter)));
		registerEvent(SaveGncEvent.class.getName(), new SaveGncCommand(view, _interactors.txImport()));
		registerEvent(SelectExpenseAccEvent.class.getName(), new SelectExpenseAccCommand(view, _interactors.accSelection(accSelectionPresenter)));
		registerEvent(CreateAccHierarchyEvent.class.getName(), new CreateAccHierarchyCommand(view, _config, _interactors.accSelection(accSelectionPresenter)));		
	}
}
