package gncimport.ui.events;

import gncimport.models.AccountData;
import gncimport.ui.Event;

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
