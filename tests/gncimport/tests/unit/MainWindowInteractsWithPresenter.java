package gncimport.tests.unit;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import gncimport.ui.GncImportMainWindow;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.junit.Test;

public class MainWindowInteractsWithPresenter extends MainWindowTests
{
	@Test
	public void requests_initizialization_from_presenter_on_construction()
	{
		GuiActionRunner.execute(new GuiQuery<GncImportMainWindow>()
		{
			protected GncImportMainWindow executeInEDT()
			{
				new GncImportMainWindow(_presenter);
				return null;
			}
		});

		verify(_presenter).onReadFromCsvFile(anyString());
		verify(_presenter).onLoadGncFile(anyString());
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

}
