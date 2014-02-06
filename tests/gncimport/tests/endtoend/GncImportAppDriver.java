package gncimport.tests.endtoend;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gncimport.boundaries.TxModel;
import gncimport.models.TxData;
import gncimport.ui.GncImport;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.fest.swing.core.Robot;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.fixture.JTableFixture;

public class GncImportAppDriver
{
	private FrameFixture _mainWindow;
	private TxModel _model;

	public GncImportAppDriver(Robot robot, int expectedTxCount)
	{
		_model = mock(TxModel.class);
		when(_model.fetchTransactions()).thenReturn(createTestTxs(expectedTxCount));
		// when(_model.getTxCount()).thenReturn(expectedTxCount);

		JFrame frame = GuiActionRunner.execute(new GuiQuery<JFrame>()
		{
			protected JFrame executeInEDT()
			{
				return GncImport.createMainFrame(_model);
			}
		});

		_mainWindow = new FrameFixture(robot, frame);
		_mainWindow.show();
	}

	private List<TxData> createTestTxs(int expectedTxCount)
	{
		ArrayList<TxData> txs = new ArrayList<TxData>();
		for (int i = 0; i < expectedTxCount; i++)
		{
			txs.add(new TxData("1/1/2014", i, "Tx " + i));
		}
		return txs;
	}

	public void shouldDisplayTransactionGridWithTransactionCount(int txCount)
	{
		JTableFixture grid = _mainWindow.table("TRANSACTION_GRID");
		assertThat(grid.rowCount(), is(txCount));
	}

	public void shouldDisplayTransactionCountInStatusBar(int txCount)
	{
		JLabelFixture label = _mainWindow.label("TX_COUNT");
		assertThat(label.text(), containsString("" + txCount + " transaction"));
	}

}
