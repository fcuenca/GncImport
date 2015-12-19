package gncimport.ui;

import gncimport.models.AccountData;

public class SelectExpenseAccEvent extends Event
{
	public final AccountData newAcc;
	public final AccountData originalAcc;

	public SelectExpenseAccEvent(AccountData newAcc, AccountData originalAcc)
	{
		this.newAcc = newAcc;
		this.originalAcc = originalAcc;
	}

}
