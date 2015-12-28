package gncimport.ui;

import gncimport.transfer.MonthlyAccountParam;

import java.util.List;

public interface UIConfig
{

	String getLastGncDirectory();
	String getLastCsvDirectory();

	void setLastGncDirectory(String path);
	void setLastCsvDirectory(String path);
	
	List<MonthlyAccountParam> getMonthlyAccounts();
}
