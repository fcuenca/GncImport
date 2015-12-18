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
		_commands.loadCsv();
	}
	
	@Override
	public void onFilterTxList(Date fromDate, Date toDate)
	{
		_commands.filterTxList(fromDate, toDate);				
	}
	
	@Override
	public void onSaveToGncFile(String fileName)
	{
		_commands.saveGnc(fileName);
	}

	@Override
	public void onLoadGncFile()
	{
		_commands.loadGnc();
	}
	
	@Override
	public void onSelectSourceAccount()
	{
		_commands.selectSourceAcc();
	}

	@Override
	public void onSelectTargetHierarchy()
	{
		_commands.selectTargetAcc();
	}

	@Override
	public void onTargetAccountSelected(AccountData newAcc, AccountData originalAcc)
	{
		_commands.selectExpenseAcc(newAcc, originalAcc);
	}

	@Override
	public void onCreateNewAccHierarchy(String fileNameToSave)
	{	
		_commands.createAccHierarchy(fileNameToSave);
	}	
}
