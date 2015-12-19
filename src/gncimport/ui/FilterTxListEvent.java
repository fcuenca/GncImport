package gncimport.ui;

import java.util.Date;

public class FilterTxListEvent
{
	public Date fromDate;
	public Date toDate;

	public FilterTxListEvent(Date fromDate, Date toDate)
	{
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
	
}