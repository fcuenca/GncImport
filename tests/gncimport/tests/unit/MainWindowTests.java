package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import gncimport.ui.EventDispatcher;
import gncimport.ui.swing.GncImportMainWindow;

import org.fest.swing.edt.GuiQuery;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;

public abstract class MainWindowTests extends FestSwingJUnitTestCase
{
	protected EventDispatcher _dispatcher;

	protected abstract class ViewDriver extends GuiQuery<GncImportMainWindow>
	{
		abstract protected void doActionsOnView(GncImportMainWindow v);

		protected GncImportMainWindow executeInEDT()
		{
			GncImportMainWindow v = new GncImportMainWindow(_dispatcher);

			doActionsOnView(v);

			return v;
		}
	}

	@Override
	protected void onSetUp()
	{
		_dispatcher = mock(EventDispatcher.class);
	}

}