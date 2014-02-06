package gncimport.tests.unit;

import static gncimport.tests.unit.ListUtils.list_of;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.boundaries.TxModel;
import gncimport.boundaries.TxView;
import gncimport.models.TxData;
import gncimport.presenters.MainWindowPresenter;
import gncimport.ui.TxTableModel;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RenderTxListAndCountInMainWindowTests
{
	@Captor
	private ArgumentCaptor<TxTableModel> expectedTxList;

	private MainWindowPresenter _presenter;
	private TxView _view;
	private TxModel _model;

	@Before
	public void SetUp()
	{
		_model = mock(TxModel.class);
		_view = mock(TxView.class);
		_presenter = new MainWindowPresenter(_model, _view);
	}

	@Test
	public void displays_available_transactions_on_init()
	{
		List<TxData> actualTxs = list_of(
				new TxData("Nov 15, 2012", 12, "Taxi ride"),
				new TxData("Dec 17, 2012", 98, "Groceries"));

		when(_model.fetchTransactions()).thenReturn(actualTxs);

		_presenter.onInit();

		verify(_view).displayTxData(expectedTxList.capture());
		verify(_view).displayTxCount(2);

		// assertThat(expectedTxList.getValue().getRowCount(), is(2));
		// assertThat(expectedTxList.getValue(),
		// containsTransactions(actualTxs));
	}

	// private Matcher<TxTableModel> containsTransactions(final List<TxData>
	// transactionList)
	// {
	// return new TypeSafeMatcher<TxTableModel>()
	// {
	// Matcher<Iterable<? extends Object>> collectionMatcher =
	// org.hamcrest.Matchers
	// .contains(transactionList.toArray());
	//
	// @Override
	// public void describeTo(final Description description)
	// {
	// collectionMatcher.describeTo(description);
	// }
	//
	// @Override
	// public boolean matchesSafely(TxTableModel item)
	// {
	// List<TxData> theTxs = new ArrayList<TxData>();
	//
	// for (int i = 0; i < transactionList.size(); i++)
	// {
	// TxData t = new TxData((String) item.getValueAt(i, 0),
	// (Double) item.getValueAt(i, 1),
	// (String) item.getValueAt(i, 2));
	// theTxs.add(t);
	// }
	//
	// return collectionMatcher.matches(theTxs);
	//
	// }
	// };
	// }

}
