package gncimport.ui;

import gncimport.interactors.TxBrowseInteractor;

import java.util.Date;

class FilterTxListCommand
{
	private Date _fromDate;
	private Date _toDate;
	private TxBrowseInteractor _theInteractor;

	public FilterTxListCommand(Date fromDate, Date toDate, TxBrowseInteractor interactor)
	{
		_fromDate = fromDate;
		_toDate = toDate;
		_theInteractor = interactor;
	}

	@SuppressWarnings("deprecation")
	public void execute()
	{	
		Date lowerBound = _fromDate != null ? _fromDate : new Date(Long.MIN_VALUE);

		Date upperBound = _toDate;
		if (upperBound != null)
		{
			upperBound = (Date) _toDate.clone();
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