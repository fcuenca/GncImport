package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import gncimport.models.AccountData;
import gncimport.ui.GncImportMainWindow;
import gncimport.ui.TxTableModel;

import java.util.ArrayList;

import javax.swing.table.TableModel;

import org.fest.swing.core.matcher.JLabelMatcher;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.fixture.JPanelFixture;
import org.fest.swing.fixture.JTableFixture;
import org.junit.Test;

public class MainWindowDisplaysTxListAndCount extends MainWindowTests
{

	@Test
	public void displays_transaction_count_in_plural()
	{
		GncImportMainWindow view = GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
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
		GncImportMainWindow view = GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
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
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.displayTxCount(-1);
			}
		});
	}

	@Test
	public void displays_transaction_grid()
	{
		final TxTableModel tableModel = new TxTableModel(SampleTxData.txDataList());

		GncImportMainWindow view = GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.displayTxData(tableModel, new ArrayList<AccountData>());
			}
		});

		JPanelFixture viewFixture = new JPanelFixture(robot(), view);

		JTableFixture grid = viewFixture.table(JTableMatcher.withName("TRANSACTION_GRID"));

		assertNotNull(grid);
		assertThat(grid.target.getRowCount(), is(tableModel.getRowCount()));
		assertThat(grid.target.getModel(), sameInstance((TableModel) tableModel));

	}
}
