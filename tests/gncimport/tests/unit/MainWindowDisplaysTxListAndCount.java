package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.boundaries.MainWindowRenderer;
import gncimport.ui.GncImport;

import org.fest.swing.core.matcher.JLabelMatcher;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.JPanelFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.junit.Test;

public class MainWindowDisplaysTxListAndCount extends FestSwingJUnitTestCase
{
	private MainWindowRenderer _presenter;

	abstract class ViewDriver extends GuiQuery<GncImport>
	{
		abstract protected void doActionsOnView(GncImport v);

		protected GncImport executeInEDT()
		{
			GncImport v = new GncImport(_presenter);

			doActionsOnView(v);

			return v;
		}
	}

	@Override
	protected void onSetUp()
	{
		_presenter = mock(MainWindowRenderer.class);
	}

	@Test
	public void requests_initizialization_from_presenter_on_construction()
	{
		GuiActionRunner.execute(new GuiQuery<GncImport>()
		{
			protected GncImport executeInEDT()
			{
				new GncImport(_presenter);
				return null;
			}
		});

		verify(_presenter).onInit();
	}

	@Test
	public void displays_transaction_count_in_plural()
	{
		GncImport view = GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImport v)
			{
				v.displayTxCount(10);
			}
		});

		JPanelFixture viewFixture = new JPanelFixture(robot(), view);

		assertThat(viewFixture.label(JLabelMatcher.withName("TX_COUNT")).text(), is("10 transactions."));
	}

}
