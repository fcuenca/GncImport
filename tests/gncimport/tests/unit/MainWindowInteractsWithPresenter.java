package gncimport.tests.unit;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import gncimport.ui.GncImportMainWindow;

import org.fest.swing.edt.GuiActionRunner;
import org.junit.Test;

public class MainWindowInteractsWithPresenter extends MainWindowTests
{
	@Test
	public void triggers_loading_of_gnc_file()
	{
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.onLoadGncFile();
			}
		});

		verify(_presenter).onLoadGncFile();
	}

	@Test
	public void triggers_loading_of_csv_file()
	{
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.onLoadCsvFile("/path/to/file.csv");
			}
		});

		verify(_presenter).onReadFromCsvFile();
	}

	@Test
	public void triggers_save_on_presenter()
	{
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.onImportClick();
			}
		});

		verify(_presenter).onSaveToGncFile(anyString());
	}

	@Test
	public void triggers_source_account_selection()
	{
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.onSelectSourceAccClick();
			}
		});

		verify(_presenter).onSelectSourceAccount();
	}

	@Test
	public void triggers_target_account_selection()
	{
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.onSelectTargetHierarchyClick();
			}
		});

		verify(_presenter).onSelectTargetHierarchy();
	}

}
