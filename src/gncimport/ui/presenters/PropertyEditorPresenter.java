package gncimport.ui.presenters;

import gncimport.interactors.PropertyEditInteractor;
import gncimport.transfer.RuleTester;
import gncimport.ui.TxView;
import gncimport.ui.swing.PropertyEditorTableModel;

import java.util.Map;

public class PropertyEditorPresenter implements PropertyEditInteractor.OutPort
{
	private TxView _view;

	public PropertyEditorPresenter(TxView view)
	{
		this._view = view;
	}

	@Override
	public boolean editProperties(Map<String, Object> allRules, RuleTester tester)
	{		
		PropertyEditorTableModel tableModel = new PropertyEditorTableModel(allRules, tester);

		boolean changesConfirmed;
		
		while(true)
		{
			changesConfirmed = _view.editProperties(tableModel);
			
			if(!changesConfirmed)
			{
				return false;
			}
			else if(tableModel.isValid())
			{
				return true;
			}
			else 
			{
				_view.displayErrorMessage("Fix invalid properties!!");
			}
		}
	}
}
