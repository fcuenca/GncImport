package gncimport.ui;

import gncimport.interactors.TxBrowseInteractor;

import java.util.Date;

class FilterTxListCommand implements Command
{
	private TxBrowseInteractor _theInteractor;

	public FilterTxListCommand(TxBrowseInteractor interactor)
	{
		_theInteractor = interactor;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void execute(Object eventArgs)
	{	
		FilterTxListEvent e = (FilterTxListEvent)eventArgs;
		
		Date lowerBound = e.fromDate != null ? e.fromDate : new Date(Long.MIN_VALUE);

		Date upperBound = e.toDate;
		if (upperBound != null)
		{
			upperBound = (Date) e.toDate.clone();
			upperBound.setHours(23);
			upperBound.setMinutes(59);
			upperBound.setSeconds(59);
		}
		else
		{
			upperBound = new Date(Long.MAX_VALUE);
		}

		_theInteractor.filterTxList(lowerBound, upperBound);;
	}
}
