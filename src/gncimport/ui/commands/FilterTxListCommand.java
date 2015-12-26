package gncimport.ui.commands;

import gncimport.interactors.TxBrowseInteractor;
import gncimport.ui.Command;
import gncimport.ui.TxView;
import gncimport.ui.events.FilterTxListEvent;

import java.util.Date;

public class FilterTxListCommand 
	extends AbstractCommand<FilterTxListEvent> implements Command<FilterTxListEvent>
{
	private TxBrowseInteractor _theInteractor;

	public FilterTxListCommand(TxView view, TxBrowseInteractor interactor)
	{
		super(view);
		_theInteractor = interactor;
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void doExecute(FilterTxListEvent event)
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
