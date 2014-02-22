package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gncimport.boundaries.TxImportModel;
import gncimport.boundaries.TxView;
import gncimport.models.AccountData;
import gncimport.models.TxData;
import gncimport.tests.data.SampleTxData;
import gncimport.ui.MainWindowPresenter;
import gncimport.ui.TxTableModel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PresenterRendersTxListAndCountInMainWindowTests
{
	@Captor
	private ArgumentCaptor<TxTableModel> expectedTxList;

	private MainWindowPresenter _presenter;
	private TxView _view;
	private TxImportModel _model;

	@Before
	public void SetUp()
	{
		_model = mock(TxImportModel.class);
		_view = mock(TxView.class);
		_presenter = new MainWindowPresenter(_model, _view);
	}

	@Test
	public void displays_available_transactions_from_file()
	{
		List<TxData> actualTxs = SampleTxData.txDataList();

		when(_model.fetchTransactionsFrom("/path/to/file.csv")).thenReturn(actualTxs);

		_presenter.onReadFromCsvFile("/path/to/file.csv");

		verify(_view).displayTxData(expectedTxList.capture(), anyListOf(AccountData.class));

		assertThat(expectedTxList.getValue().getRowCount(), is(actualTxs.size()));
		assertThat(expectedTxList.getValue(), containsTransactions(actualTxs));
	}

	@Test
	public void displays_transaction_count_on_file_open()
	{
		List<TxData> actualTxs = SampleTxData.txDataList();

		when(_model.fetchTransactionsFrom("/path/to/file.csv")).thenReturn(actualTxs);

		_presenter.onReadFromCsvFile("/path/to/file.csv");

		verify(_view).displayTxCount(2);
	}

	private Matcher<TxTableModel> containsTransactions(final List<TxData> transactionList)
	{
		return new TypeSafeMatcher<TxTableModel>()
		{
			Matcher<Iterable<? extends Object>> collectionMatcher = buildExpectedResult(transactionList);

			private Matcher<Iterable<? extends Object>> buildExpectedResult(final List<TxData> transactionList)
			{
				List<String> visibleFields = new ArrayList<String>();

				for (TxData t : transactionList)
				{
					visibleFields.add("[" +
							t.date + ", " +
							t.amount.setScale(2) + ", " +
							t.description + "]");
				}

				return org.hamcrest.Matchers
						.contains(visibleFields.toArray());
			}

			@Override
			public void describeTo(final Description description)
			{
				collectionMatcher.describeTo(description);
			}

			@Override
			public boolean matchesSafely(TxTableModel item)
			{
				SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
				List<String> visibleFields = new ArrayList<String>();

				for (int i = 0; i < item.getRowCount(); i++)
				{
					try
					{
						String s = "[" +
								dateFormatter.parse((String) item.getValueAt(i, 0)) + ", " +
								new BigDecimal((String) item.getValueAt(i, 1)).setScale(2) + ", " +
								(String) item.getValueAt(i, 2) + "]";
						visibleFields.add(s);
					}
					catch (ParseException e)
					{
						fail("Parse error: " + e.getMessage());
					}
				}

				return collectionMatcher.matches(visibleFields);

			}
		};
	}
}
