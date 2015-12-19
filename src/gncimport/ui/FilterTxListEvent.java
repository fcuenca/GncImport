package gncimport.ui;

import java.util.Date;

public class FilterTxListEvent extends Event
{
	public final Date fromDate;
	public final Date toDate;

	public FilterTxListEvent(Date fromDate, Date toDate)
	{
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
	
}