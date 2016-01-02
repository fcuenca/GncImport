package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.interactors.PropertyEditInteractor;
import gncimport.ui.TxView;
import gncimport.ui.commands.EditPropertiesCommand;

import org.junit.Test;

public class EditPropertiesCommandTests
{

	@Test
	public void forwards_request_to_interactor()
	{
		TxView view = mock(TxView.class);
		PropertyEditInteractor interactor = mock(PropertyEditInteractor.class);

		EditPropertiesCommand cmd = new EditPropertiesCommand(view, interactor);
		
		cmd.execute(null);
		
		verify(interactor).displayCurrentConfig();
	}
}
