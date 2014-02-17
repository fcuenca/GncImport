package gncimport.tests.unit;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import gncimport.ui.GncImportMainWindow;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.junit.Test;
import org.mockito.InOrder;

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

		InOrder inOrder = inOrder(_presenter);

		inOrder.verify(_presenter).onLoadGncFile(anyString());
		inOrder.verify(_presenter).onReadFromCsvFile(anyString());
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

}
