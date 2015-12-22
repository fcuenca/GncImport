package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.ui.SelectTargetAccCommand;
import gncimport.ui.TxView;

import org.junit.Test;

public class SelectTargetAccCommandTests
{
	@Test
	public void forwards_request_to_interactor()
	{
		TxView view = mock(TxView.class);
		AccSelectionInteractor interactor = mock(AccSelectionInteractor.class);
		
		SelectTargetAccCommand cmd = new SelectTargetAccCommand(view, interactor);
		
		cmd.execute(null);
		
		verify(interactor).selectTargetAccount();
	}
}
