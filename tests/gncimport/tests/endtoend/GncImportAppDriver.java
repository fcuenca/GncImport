package gncimport.tests.endtoend;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import gncimport.GncImportApp;
import gncimport.transfer.TxData;
import gncimport.ui.swing.GncImportMainWindow;

import java.io.File;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import org.fest.swing.core.Robot;
import org.fest.swing.data.TableCell;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JComboBoxFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.fixture.JTabbedPaneFixture;
import org.fest.swing.fixture.JTableCellFixture;
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
				GncImportApp.DEFAULT_TARGET_ACCOUNT = "Miscelaneous";
				
				return GncImportApp.createMainFrame();
			}
		});

		_mainWindow = new FrameFixture(robot, frame);
		_mainWindow.show();
	}
	
	public void shutdown()
	{
		_mainWindow.cleanUp();
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
	
	public void shouldDisplayTransactionWithDescription(String txText)
	{
		int matchesFound = 0;
		
		JTableFixture grid = _mainWindow.table("TRANSACTION_GRID");
		
		for (int i = 0; i < grid.rowCount(); i++)
		{
			if(grid.target.getValueAt(i, 2).toString().trim().matches(txText))
			{
				matchesFound++;
			}
		}
		
		assertThat("couldn't find any matches for: '" + txText + "'", matchesFound, not(is(0)));
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
	
	public void shouldAllowSelectionOfExpenseAccountsInRow(int rowNumber, String[] expectedAccNames)
	{
		JTableFixture grid = _mainWindow.table("TRANSACTION_GRID");
		JTableCellFixture cell = grid.cell(TableCell.row(rowNumber).column(3));	
		JComboBoxFixture editor = new JComboBoxFixture(_mainWindow.robot, (JComboBox) cell.editor());
		
		assertThat(editor.contents(), is(arrayContaining(expectedAccNames)));		
	}
	
	public void selectExpenseAccountForTransactionInRow(int rowNumber, String accName)
	{
		JTableFixture grid = _mainWindow.table("TRANSACTION_GRID");
		JTableCellFixture cell = grid.cell(TableCell.row(rowNumber).column(3));
		JComboBoxFixture editor = new JComboBoxFixture(_mainWindow.robot, (JComboBox) cell.editor());
				
		cell.click();
		editor.selectItem(accName);
	}

	public void shouldDisplayAccountForTransactionInRow(int rowNumber, String expectedAccName)
	{
		JTableFixture grid = _mainWindow.table("TRANSACTION_GRID");
		assertThat(grid.target.getValueAt(rowNumber, 3).toString(), is(expectedAccName));		
	}

	public void shouldNotIgnoreAnyTransaction()
	{
		int importCount = 0;
		
		JTableFixture grid = _mainWindow.table("TRANSACTION_GRID");
		
		for (int i = 0; i < grid.rowCount(); i++)
		{
			assertThat("unexpected data type in 'Ignore' column",
					grid.target.getValueAt(i, 4), is(instanceOf(Boolean.class)));
			
			if(grid.target.getValueAt(i, 4).equals(false))
			{
				importCount++;
			}
		}
		
		assertThat("not all rows are flagged as imported", importCount, is(grid.rowCount()));
	}

	public void shouldIgnoreTransactionsLike(String txDesc)
	{
		JTableFixture grid = _mainWindow.table("TRANSACTION_GRID");
		
		for (int i = 0; i < grid.rowCount(); i++)
		{
			assertThat("unexpected data type in 'Ignore' column",
					grid.target.getValueAt(i, 4), is(instanceOf(Boolean.class)));
			
			if (grid.target.getValueAt(i, 2).toString().matches(txDesc))
			{
				assertThat("Unexpected ignore status for row " + i + ": " + grid.target.getValueAt(i, 2),
						(Boolean)grid.target.getValueAt(i, 4), is(true));
			}
			else
			{
				assertThat("Unexpected ignore status for row " + i + ": " + grid.target.getValueAt(i, 2),
						(Boolean)grid.target.getValueAt(i, 4), is(false));
			}
		}
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
	
	public void createAccountHierarchy(String[] nodesToParent, String rootAccount)
	{		
		_mainWindow.menuItem("NEW_ACC_HIERARCHY").click();

		DialogFixture dialog = _mainWindow.dialog("NEW_HIERARCHY_DLG");
		
		JTreeFixture tree = dialog.tree("ACC_TREE");
		String path = "";
		for (int i = 0; i < nodesToParent.length - 1; i++)
		{
			path += nodesToParent[i] + "/";
			tree.node(path).expand();
		}
		path += nodesToParent[nodesToParent.length - 1];
		tree.node(path).click();
		
		dialog.textBox("ROOT_ACC_FIELD").setText(rootAccount);
		
		dialog.button("OK_BUTTON").click();
	}

	private void selectFromAccountTree(String selectionBtnName, String[] nodes)
	{
		_mainWindow.button(selectionBtnName).click();
		
		DialogFixture dialog = _mainWindow.dialog("ACC_SELECTION_DLG");
		JTreeFixture tree = dialog.tree("ACC_TREE");
		
		String path = "";
		
		for (int i = 0; i < nodes.length - 1; i++)
		{
			path += nodes[i] + "/";
			tree.node(path).expand();
		}
		
		path += nodes[nodes.length - 1];
		tree.node(path).click();
		
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

	public void shouldLookForFilesInFolder(String path)
	{
		_mainWindow.button(GncImportMainWindow.OPEN_CSV_BUTTON).click();
		assertFileChooserDirectoryEquals(path);
		
		_mainWindow.button(GncImportMainWindow.OPEN_GNC_BUTTON).click();
		assertFileChooserDirectoryEquals(path);
	}

	private void assertFileChooserDirectoryEquals(String path)
	{
		JFileChooserFixture fileChooser = _mainWindow.fileChooser("FILE_CHOOSER");
		assertThat(fileChooser.target.getCurrentDirectory().getPath(), is(path));
		fileChooser.cancel();
	}

	public void shouldDisplayTransactions(List<TxData> list)
	{
		JTableFixture grid = _mainWindow.table("TRANSACTION_GRID");
		assertThat(grid.rowCount(), is(list.size()));

		for (int i = 0; i < grid.rowCount(); i++)
		{
			String txDescription = list.get(i).description;
			
			assertThat("Mismatch found at row: " + i,
					grid.target.getValueAt(i, 2).toString(), is(txDescription));
		}
	}

	public void editPropertiesAndExpectRowsToMatch(int expectedIgnoreRules, int expectedAccOverrideRules, int expectedTxOverrideRules, int expectedMonthlyAccs)
	{
		_mainWindow.menuItem("EDIT_PROPERTIES").click();

		DialogFixture dialog = _mainWindow.dialog("PROP_EDITOR_DLG");
		
		JTabbedPaneFixture tabs = dialog.tabbedPane("PROPERTY_TABS");
		tabs.selectTab("Ignore");
		
		JTableFixture table = dialog.table("IGNORE_RULES");
		assertThat(table.rowCount(), is(expectedIgnoreRules));

		tabs.selectTab("Acc Override");
		table = dialog.table("ACC_OVERRIDE_RULES");
		assertThat(table.rowCount(), is(expectedAccOverrideRules)); 
		
		tabs.selectTab("Tx Override");
		table = dialog.table("TX_OVERRIDE_RULES");
		assertThat(table.rowCount(), is(expectedTxOverrideRules)); 
		
		tabs.selectTab("Monthy Accs");
		table = dialog.table("MONTHLY_ACCS_RULES");
		assertThat(table.rowCount(), is(expectedMonthlyAccs)); 
		
		dialog.button("OK_BUTTON").click();
	}

}
