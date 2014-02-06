package gncimport.tests.endtoend;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gncimport.boundaries.TxModel;
import gncimport.ui.GncImport;

import javax.swing.JFrame;

import org.fest.swing.core.Robot;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;

public class GncImportAppDriver
{
	private FrameFixture _mainWindow;
	private TxModel _model;

	public GncImportAppDriver(Robot robot, int expectedTxCount)
	{
		_model = mock(TxModel.class);
		when(_model.getTxCount()).thenReturn(expectedTxCount);

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

	public void shouldDisplayTransactionGridWithTransactionCount(int txCount)
	{
		fail("niy");
	}

	public void shouldDisplayTransactionCountInStatusBar(int txCount)
	{
		JLabelFixture label = _mainWindow.label("TX_COUNT");
		assertThat(label.text(), containsString("" + txCount + " transaction"));
	}
}
