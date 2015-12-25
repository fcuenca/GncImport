package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import gncimport.models.AccountData;
import gncimport.ui.CreateAccHierarchyEvent;
import gncimport.ui.FilterTxListEvent;
import gncimport.ui.GncImportMainWindow;
import gncimport.ui.NoArgsEvent;
import gncimport.ui.SaveGncEvent;
import gncimport.ui.SelectExpenseAccEvent;

import org.fest.swing.edt.GuiActionRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MainWindowInteractsWithPresenter extends MainWindowTests
{
	@Captor
	private ArgumentCaptor<SelectExpenseAccEvent> expectedExpenseSelectEvent;
	@Captor
	private ArgumentCaptor<SaveGncEvent> expectedSaveGncEvent;
	@Captor
	private ArgumentCaptor<CreateAccHierarchyEvent> expectedCreateAccHierarchyEvent;

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

		verify(_commands).triggerWithoutArgs(NoArgsEvent.LoadGncEvent);;
	}

	@Test
	public void triggers_loading_of_csv_file()
	{
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.onLoadCsvFile();
			}
		});

		verify(_commands).triggerWithoutArgs(NoArgsEvent.LoadCsvEvent);;		
	}
	
	
	@Test
	public void triggers_save_on_presenter()
	{
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.updateGncFileLabel("file.gnc");
				v.onImportClick();
			}
		});

		verify(_commands).triggerWithArgs(expectedSaveGncEvent.capture());
		assertThat(expectedSaveGncEvent.getValue().fileName, is("file.gnc"));
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

		verify(_commands).triggerWithoutArgs(NoArgsEvent.SelectSourceAccEvent);
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

		verify(_commands).triggerWithoutArgs(NoArgsEvent.SelectTargetAccEvent);
	}
	
	@Test
	public void triggers_new_acc_hierarchy_creation()
	{
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.updateGncFileLabel("file.gnc");
				v.onNewAccHierarchy();
			}
		});

		verify(_commands).triggerWithArgs(expectedCreateAccHierarchyEvent.capture());
		assertThat(expectedCreateAccHierarchyEvent.getValue().fileNameToSave, is("file.gnc"));
	}
	
	@Test
	public void triggers_selection_of_expense_account_for_transaction()
	{
		final AccountData selectedAcc = new AccountData("selected", "id-1");
		final AccountData originalAcc = new AccountData("original", "id-2");
		
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.onSelectExpenseAccount(selectedAcc, originalAcc);
			}
		});
		
		verify(_commands).triggerWithArgs(expectedExpenseSelectEvent.capture());
		assertThat(expectedExpenseSelectEvent.getValue().newAcc, is(selectedAcc));
		assertThat(expectedExpenseSelectEvent.getValue().originalAcc, is(originalAcc));
	}
	
	@Test
	public void triggers_transaction_filtering()
	{
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.onFilterTxList();
			}
		});
		
		// not much can be verified here in the event arguments without
		// actually manipulating the buttons and pickers in the UI (we'll not go there....)
		verify(_commands).triggerWithArgs(any(FilterTxListEvent.class));
	}
}
