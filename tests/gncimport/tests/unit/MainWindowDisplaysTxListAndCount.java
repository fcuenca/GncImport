package gncimport.tests.unit;

import static gncimport.tests.unit.ListUtils.list_of;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.boundaries.MainWindowRenderer;
import gncimport.models.TxData;
import gncimport.ui.GncImport;
import gncimport.ui.TxTableModel;

import java.util.List;

import javax.swing.table.TableModel;

import org.fest.swing.core.matcher.JLabelMatcher;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.JPanelFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.junit.Test;

public class MainWindowDisplaysTxListAndCount extends FestSwingJUnitTestCase
{
	private MainWindowRenderer _presenter;

	abstract class ViewDriver extends GuiQuery<GncImport>
	{
		abstract protected void doActionsOnView(GncImport v);

		protected GncImport executeInEDT()
		{
			GncImport v = new GncImport(_presenter);

			doActionsOnView(v);

			return v;
		}
	}

	@Override
	protected void onSetUp()
	{
		_presenter = mock(MainWindowRenderer.class);
	}

	@Test
	public void requests_initizialization_from_presenter_on_construction()
	{
		GuiActionRunner.execute(new GuiQuery<GncImport>()
		{
			protected GncImport executeInEDT()
			{
				new GncImport(_presenter);
				return null;
			}
		});

		verify(_presenter).onInit();
	}

	@Test
	public void displays_transaction_count_in_plural()
	{
		GncImport view = GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImport v)
			{
				v.displayTxCount(10);
			}
		});

		JPanelFixture viewFixture = new JPanelFixture(robot(), view);

		assertThat(viewFixture.label(JLabelMatcher.withName("TX_COUNT")).text(), is("10 transactions."));
	}

	@Test
	public void displays_transaction_count_in_singular()
	{
		GncImport view = GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImport v)
			{
				v.displayTxCount(1);
			}
		});

		JPanelFixture viewFixture = new JPanelFixture(robot(), view);

		assertThat(viewFixture.label(JLabelMatcher.withName("TX_COUNT")).text(), is("1 transaction."));
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejects_negative_transaction_count()
	{
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImport v)
			{
				v.displayTxCount(-1);
			}
		});
	}

	@Test
	public void displays_transaction_grid()
	{
		List<TxData> actualTxs = list_of(
				new TxData("Nov 15, 2012", 12, "Taxi ride"),
				new TxData("Dec 17, 2012", 98, "Groceries"));

		final TxTableModel tableModel = new TxTableModel(actualTxs);

		GncImport view = GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImport v)
			{
				v.displayTxData(tableModel);
			}
		});

		JPanelFixture viewFixture = new JPanelFixture(robot(), view);

		JTableFixture grid = viewFixture.table(JTableMatcher.withName("TRANSACTION_GRID"));

		assertNotNull(grid);
		assertThat(grid.target.getRowCount(), is(tableModel.getRowCount()));
		assertThat(grid.target.getModel(), sameInstance((TableModel) tableModel));

	}
}
