package gncimport.tests.unit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.interactors.TxBrowseInteractor;
import gncimport.ui.TxView;
import gncimport.ui.commands.FilterTxListCommand;
import gncimport.ui.events.FilterTxListEvent;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class FilterTxListCommandTests
{
	private TxBrowseInteractor _interactor;
	private FilterTxListCommand _cmd;

	@Before
	public void SetUp()
	{
		_interactor = mock(TxBrowseInteractor.class);
		_cmd = new FilterTxListCommand(mock(TxView.class), _interactor);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void adjusts_upper_bound_to_make_last_day_all_inclusive()
	{
		Date fromDate = new Date(2012 - 1900, 10, 14, 5, 30, 27);
		Date toDate = new Date(2012 - 1900, 10, 16, 16, 33, 13);

		Date adjustedToDate = (Date) toDate.clone();
		adjustedToDate.setHours(23);
		adjustedToDate.setMinutes(59);
		adjustedToDate.setSeconds(59);
		
		_cmd.execute(new FilterTxListEvent(fromDate, toDate));

		verify(_interactor).filterTxList(fromDate, adjustedToDate);
	}
	
	@Test
	public void lower_bound_can_be_open()
	{
		_cmd.execute(new FilterTxListEvent(null, new Date()));

		Date distantPast = new Date(Long.MIN_VALUE);

		verify(_interactor).filterTxList(eq(distantPast), any(Date.class));
	}

	@Test
	public void upper_bound_can_be_open()
	{
		_cmd.execute(new FilterTxListEvent(new Date(), null));

		Date distantFuture = new Date(Long.MAX_VALUE);

		verify(_interactor).filterTxList(any(Date.class), eq(distantFuture));
	}


}
