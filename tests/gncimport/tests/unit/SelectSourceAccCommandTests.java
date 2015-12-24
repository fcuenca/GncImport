package gncimport.tests.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.interactors.AccSelectionInteractor;
import gncimport.ui.SelectSourceAccCommand;
import gncimport.ui.TxView;

import org.junit.Test;

public class SelectSourceAccCommandTests
{

	@Test
	public void forwards_request_to_interactor()
	{
		TxView view = mock(TxView.class);
		AccSelectionInteractor interactor = mock(AccSelectionInteractor.class);
		
		SelectSourceAccCommand cmd = new SelectSourceAccCommand(view, interactor);
		
		cmd.execute(null);
		
		verify(interactor).selectSourceAccount();
	}

}
