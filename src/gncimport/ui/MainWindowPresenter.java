package gncimport.ui;

import gncimport.interactors.InteractorFactory;
import gncimport.models.AccountData;
import gncimport.models.TxImportModel;

import java.util.Date;

public class MainWindowPresenter implements MainWindowRenderer
{	
	private final CommandFactory _commands;
	
	public MainWindowPresenter(TxImportModel model, TxView view, UIConfig config)
	{
		_commands = new CommandFactory(view, config, new InteractorFactory(model));
	}
		
	@Override
	public void onReadFromCsvFile()
	{
		_commands.triggerWithoutArgs(NoArgsEvent.LoadCsvEvent);
	}
	
	@Override
	public void onFilterTxList(Date fromDate, Date toDate)
	{
		_commands.triggerWithArgs(new FilterTxListEvent(fromDate, toDate));
	}
	
	@Override
	public void onSaveToGncFile(String fileName)
	{
		_commands.triggerWithArgs(new SaveGncEvent(fileName));
	}

	@Override
	public void onLoadGncFile()
	{
		_commands.triggerWithoutArgs(NoArgsEvent.LoadGncEvent);
	}
	
	@Override
	public void onSelectSourceAccount()
	{
		_commands.triggerWithoutArgs(NoArgsEvent.SelectSourceAccEvent);
	}

	@Override
	public void onSelectTargetHierarchy()
	{
		_commands.triggerWithoutArgs(NoArgsEvent.SelectTargetAccEvent);
	}

	@Override
	public void onTargetAccountSelected(AccountData newAcc, AccountData originalAcc)
	{
		_commands.triggerWithArgs(new SelectExpenseAccEvent(newAcc, originalAcc));
	}

	@Override
	public void onCreateNewAccHierarchy(String fileNameToSave)
	{	
		_commands.triggerWithArgs(new CreateAccHierarchyEvent(fileNameToSave));
	}	
}
