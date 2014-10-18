package gncimport.tests.endtoend;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import gncimport.GncImportApp;
import gncimport.ui.GncImportMainWindow;

import java.io.File;

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
	private FrameFixture _mainWindow;

	public GncImportAppDriver(Robot robot)
	{	
		JFrame frame = GuiActionRunner.execute(new GuiQuery<JFrame>()
		{
			protected JFrame executeInEDT()
			{
				return GncImportApp.createMainFrame();
			}
		});

		_mainWindow = new FrameFixture(robot, frame);
		_mainWindow.show();
	}

	public void openTestInputFiles(String gncFilePath, String csvFilePath)
	{
		openGncFile(gncFilePath);
		openCsvFile(csvFilePath);
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

	public void importTransactions()
	{
		_mainWindow.button(GncImportMainWindow.IMPORT_BUTTON).click();
	}

	private void openCsvFile(String csvFilePath)
	{
		_mainWindow.button(GncImportMainWindow.OPEN_CSV_BUTTON).click();
		selectFileInChooser(csvFilePath);
	}

	private void openGncFile(String gncFilePath)
	{
		_mainWindow.button(GncImportMainWindow.OPEN_GNC_BUTTON).click();
		selectFileInChooser(gncFilePath);
	
		selectCheckingAccountFromTree();
		selectFebreroAccountFromTree();
	}

	protected void selectFebreroAccountFromTree()
	{
		_mainWindow.button(GncImportMainWindow.SELECT_TARGET_ACC_BUTTON).click();
		
		DialogFixture dialog = _mainWindow.dialog("ACC_SELECTION_DLG");
		JTreeFixture tree = dialog.tree("ACC_TREE");

		tree.node("Gastos Mensuales").expand();
		tree.node("Gastos Mensuales/Year 2014").expand();
		tree.node("Gastos Mensuales/Year 2014/Febrero 2014").expand();

		dialog.button("OK_BUTTON").click();
	}

	protected void selectCheckingAccountFromTree()
	{
		_mainWindow.button(GncImportMainWindow.SELECT_SRC_ACC_BUTTON).click();
		
		DialogFixture dialog = _mainWindow.dialog("ACC_SELECTION_DLG");
		JTreeFixture tree = dialog.tree("ACC_TREE");

		tree.node("Assets").expand();
		tree.node("Assets/Current Assets").expand();
		tree.node("Assets/Current Assets/Checking Account").expand();

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
