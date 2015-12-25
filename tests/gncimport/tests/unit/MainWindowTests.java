package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import gncimport.ui.CommandFactory;
import gncimport.ui.GncImportMainWindow;
import gncimport.ui.MainWindowRenderer;

import org.fest.swing.edt.GuiQuery;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;

public abstract class MainWindowTests extends FestSwingJUnitTestCase
{
	protected MainWindowRenderer _presenter;
	protected CommandFactory _commands;

	protected abstract class ViewDriver extends GuiQuery<GncImportMainWindow>
	{
		abstract protected void doActionsOnView(GncImportMainWindow v);

		protected GncImportMainWindow executeInEDT()
		{
			GncImportMainWindow v = new GncImportMainWindow(_presenter, _commands);

			doActionsOnView(v);

			return v;
		}
	}

	@Override
	protected void onSetUp()
	{
		_commands = mock(CommandFactory.class);
		_presenter = mock(MainWindowRenderer.class);
	}

}