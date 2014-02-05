package gncimport.tests.endtoend;

import static org.junit.Assert.fail;
import gncimport.ui.GncImport;

import javax.swing.JFrame;

import org.fest.swing.core.Robot;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;

public class GncImportAppDriver
{
	private FrameFixture _mainWindow;

	public GncImportAppDriver(Robot robot)
	{
		JFrame frame = GuiActionRunner.execute(new GuiQuery<JFrame>()
		{
			protected JFrame executeInEDT()
			{
				return GncImport.createMainFrame();
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
		fail("niy");
	}
}
