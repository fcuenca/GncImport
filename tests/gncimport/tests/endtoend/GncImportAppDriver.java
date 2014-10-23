package gncimport.tests.endtoend;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
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
				GncImportApp.HomeDir = "/tmp";
				
				return GncImportApp.createMainFrame();
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
		JLabelFixture label = _mainWindow.label("TX_COUNT");
		assertThat(label.text(), containsString("" + txCount + " transaction"));
	}
	
	public void shouldAssociateAllTransactionsTo(String accountName)
	{
		JTableFixture grid = _mainWindow.table("TRANSACTION_GRID");
		
		for (int i = 0; i < grid.rowCount(); i++)
		{
			assertThat("row: "+ i , grid.target.getValueAt(i, 3).toString(), is(accountName));
		}
	}
	
	public void shouldAssociateTransactionsToAccount(String txText, String accountName)
	{
		int matchesFound = 0;
		
		JTableFixture grid = _mainWindow.table("TRANSACTION_GRID");
		
		for (int i = 0; i < grid.rowCount(); i++)
		{
			if(grid.target.getValueAt(i, 2).toString().trim().equals(txText))
			{
				matchesFound++;
				assertThat("row: "+ i + ", '" + txText + "'" , grid.target.getValueAt(i, 3).toString(), is(accountName));
			}
		}
		
		assertThat("couldn't find any matches for: '" + txText + "'", matchesFound, not(is(0)));
	}

	public void importTransactions()
	{
		_mainWindow.button(GncImportMainWindow.IMPORT_BUTTON).click();
	}

	public void openCsvFile(String csvFilePath)
	{
		_mainWindow.button(GncImportMainWindow.OPEN_CSV_BUTTON).click();
		selectFileInChooser(csvFilePath);
	}

	public void openGncFile(String gncFilePath)
	{
		_mainWindow.button(GncImportMainWindow.OPEN_GNC_BUTTON).click();
		selectFileInChooser(gncFilePath);	
	}

	public void selectTargetAccount(String[] treeNodeSequence)
	{
		selectFromAccountTree(GncImportMainWindow.SELECT_TARGET_ACC_BUTTON, treeNodeSequence);
	}

	public void selectSourceAccount(String[] treeNodeSequence)
	{
		selectFromAccountTree(GncImportMainWindow.SELECT_SRC_ACC_BUTTON, treeNodeSequence);
	}
	
	private void selectFromAccountTree(String selectionBtnName, String[] nodes)
	{
		_mainWindow.button(selectionBtnName).click();
		
		DialogFixture dialog = _mainWindow.dialog("ACC_SELECTION_DLG");
		JTreeFixture tree = dialog.tree("ACC_TREE");
		
		String path = "";
		for (String n : nodes)
		{
			path += n + "/";
			tree.node(path).expand();
		}
		
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
