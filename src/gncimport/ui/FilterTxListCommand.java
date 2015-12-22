package gncimport.ui;

import gncimport.interactors.TxBrowseInteractor;

import java.util.Date;

public class FilterTxListCommand implements Command<FilterTxListEvent>
{
	private TxBrowseInteractor _theInteractor;

	public FilterTxListCommand(TxBrowseInteractor interactor)
	{
		_theInteractor = interactor;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void execute(FilterTxListEvent event)
	{			
		Date lowerBound = event.fromDate != null ? event.fromDate : new Date(Long.MIN_VALUE);

		Date upperBound = event.toDate;
		if (upperBound != null)
		{
			upperBound = (Date) event.toDate.clone();
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
