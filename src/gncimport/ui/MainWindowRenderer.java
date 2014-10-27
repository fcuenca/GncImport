package gncimport.ui;

import gncimport.models.AccountData;

import java.util.Date;

public interface MainWindowRenderer
{

	public abstract void onReadFromCsvFile();

	public abstract void onSaveToGncFile(String fileName);

	public abstract void onLoadGncFile();

	public abstract void onSelectSourceAccount();

	public void onCreateNewAccHierarchy(String saveToFile);

	public abstract void onSelectTargetHierarchy();

	public AccountData onTargetAccountSelected(AccountData newAcc, AccountData originalAcc);

	public void onFilterTxList(Date fromDate, Date toDate);
}