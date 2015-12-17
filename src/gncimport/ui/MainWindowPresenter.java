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
}
