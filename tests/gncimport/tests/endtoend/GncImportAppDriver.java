package gncimport.tests.endtoend;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import gncimport.GncImportApp;
import gncimport.boundaries.TxImportModel;
import gncimport.models.LocalFileTxImportModel;
import gncimport.ui.GncImportMainWindow;

import java.io.File;

import javax.swing.JFrame;

import org.fest.swing.core.Robot;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.fixture.JTableFixture;

public class GncImportAppDriver
{
	private FrameFixture _mainWindow;
	private TxImportModel _model;

	public GncImportAppDriver(Robot robot, int expectedTxCount)
	{
		_model = new LocalFileTxImportModel();

		JFrame frame = GuiActionRunner.execute(new GuiQuery<JFrame>()
		{
			protected JFrame executeInEDT()
			{
				return GncImportApp.createMainFrame(_model);
			}
		});

		_mainWindow = new FrameFixture(robot, frame);
		_mainWindow.show();
	}

	public void shouldDisplayTransactionGridWithTransactionCount(int txCount)
	{
		JTableFixture grid = _mainWindow.table("TRANSACTION_GRID");
		assertThat(grid.rowCount(), is(txCount));
	}

	public void shouldDisplayTransactionCountInStatusBar(int txCount)
	{
		_mainWindow.button(GncImportMainWindow.OPEN_GNC_BUTTON).click();
		selectFileInChooser(getClass().getResource("../data/checkbook.xml").getPath());

		_mainWindow.button(GncImportMainWindow.OPEN_CSV_BUTTON).click();
		selectFileInChooser(getClass().getResource("../data/rbc.csv").getPath());

		JLabelFixture label = _mainWindow.label("TX_COUNT");
		assertThat(label.text(), containsString("" + txCount + " transaction"));
	}

	private void selectFileInChooser(String filePath)
	{
		JFileChooserFixture fileChooser = _mainWindow.fileChooser("FILE_CHOOSER");
		File testFile = new File(filePath);
		fileChooser.setCurrentDirectory(testFile.getParentFile());
		fileChooser.click(); // without this, the "Open button" is not enabled!
		fileChooser.selectFile(testFile);
		fileChooser.approveButton().click(); // this forces the dialog to close!
	}

	public void shouldSaveTransactionsToGncFile()
	{
		File output = new File("/tmp/checkbook-new.xml");

		if (output.exists())
		{
			assertThat("Leftover file couldn't be deleted", output.delete(), is(true));
		}

		_mainWindow.button(GncImportMainWindow.OPEN_GNC_BUTTON).click();
		selectFileInChooser(getClass().getResource("../data/checkbook.xml").getPath());

		_mainWindow.button(GncImportMainWindow.OPEN_CSV_BUTTON).click();
		selectFileInChooser(getClass().getResource("../data/rbc.csv").getPath());

		_mainWindow.button(GncImportMainWindow.IMPORT_BUTTON).click();

		assertThat("Output hasn't been created", output.exists(), is(true));
	}

}
