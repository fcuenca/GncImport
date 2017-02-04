package gncimport.ui;

import gncimport.transfer.MonthlyAccount;

import java.util.List;

public interface UIConfig
{

	String getLastGncDirectory();
	String getLastCsvDirectory();

	void setLastGncDirectory(String path);
	void setLastCsvDirectory(String path);
	
	List<MonthlyAccount> getMonthlyAccounts();
}
