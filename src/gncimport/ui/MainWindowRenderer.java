package gncimport.ui;

import gncimport.models.AccountData;

public interface MainWindowRenderer
{

	public abstract void onReadFromCsvFile(String fileName);

	public abstract void onSaveToGncFile(String fileName);

	public abstract void onLoadGncFile(String fileName);

	public abstract void onSelectSourceAccount();

	public abstract void onSelectTargetHierarchy();

	AccountData onTargetAccountSelected(AccountData newAcc, AccountData originalAcc);

}