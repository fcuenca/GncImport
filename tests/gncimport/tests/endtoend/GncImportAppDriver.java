package gncimport.tests.endtoend;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import gncimport.GncImportApp;
import gncimport.models.LocalFileTxImportModel;
import gncimport.models.TxImportModel;
import gncimport.tests.data.TestDataConfig;
import gncimport.ui.GncImportMainWindow;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.fest.swing.core.Robot;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTreeFixture;

public class GncImportAppDriver
{
	private static final String TEST_OUTPUT_FILE = "/tmp/checkbook-new.xml";

	private FrameFixture _mainWindow;
	private TxImportModel _model;

	class LocalFileTxImportModel_ForTesting extends LocalFileTxImportModel
	{
		public LocalFileTxImportModel_ForTesting(String defaultTargetAccName)
		{
			super(defaultTargetAccName);
		}

		@Override
		protected void saveToGncFile(String fileName) throws IOException
		{
			// Prevent overriding the original test file, which is the app's
			// normal behavior
			super.saveToGncFile(TEST_OUTPUT_FILE);
		}
	}

	public GncImportAppDriver(Robot robot)
	{
		_model = new LocalFileTxImportModel_ForTesting(TestDataConfig.DEFAULT_TARGET_ACCOUNT);

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

	public void openTestInputFiles()
	{
		openGncFile();
		openCsvFile();
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

	public void shouldSaveTransactionsToGncFile()
	{
		File output = new File(TEST_OUTPUT_FILE);

		if (output.exists())
		{
			assertThat("Leftover file couldn't be deleted", output.delete(), is(true));
		}

		_mainWindow.button(GncImportMainWindow.IMPORT_BUTTON).click();

		assertThat("Output hasn't been created", output.exists(), is(true));
	}

	private void openCsvFile()
	{
		_mainWindow.button(GncImportMainWindow.OPEN_CSV_BUTTON).click();
		selectFileInChooser(getClass().getResource("../data/rbc.csv").getPath());
	}

	private void openGncFile()
	{
		_mainWindow.button(GncImportMainWindow.OPEN_GNC_BUTTON).click();
		selectFileInChooser(getClass().getResource("../data/checkbook.xml").getPath());

		_mainWindow.button(GncImportMainWindow.SELECT_SRC_ACC_BUTTON).click();

		selectCheckingAccountFromTree();

		_mainWindow.button(GncImportMainWindow.SELECT_TARGET_ACC_BUTTON).click();

		selectFebreroAccountFromTree();
	}

	protected void selectFebreroAccountFromTree()
	{
		DialogFixture dialog = _mainWindow.dialog("ACC_SELECTION_DLG");
		JTreeFixture tree = dialog.tree("ACC_TREE");

		tree.node(3).expand();
		tree.node(4).expand();
		tree.node(6).select();

		dialog.button("OK_BUTTON").click();
	}

	protected void selectCheckingAccountFromTree()
	{
		DialogFixture dialog = _mainWindow.dialog("ACC_SELECTION_DLG");
		JTreeFixture tree = dialog.tree("ACC_TREE");

		tree.node(0).expand();
		tree.node(1).expand();
		tree.node(2).select();

		dialog.button("OK_BUTTON").click();
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
}
