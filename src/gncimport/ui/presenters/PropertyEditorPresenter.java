package gncimport.ui.presenters;

import gncimport.interactors.PropertyEditInteractor;
import gncimport.transfer.RuleTester;
import gncimport.ui.TxView;
import gncimport.ui.swing.PropertyEditorTableModel;

import java.util.HashMap;
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
		Map<String, PropertyEditorTableModel> modelMap = new HashMap<String, PropertyEditorTableModel>();
		modelMap.put("ignore", new PropertyEditorTableModel(allRules, tester));
		
		boolean changesConfirmed;
		
		while(true)
		{
			changesConfirmed = _view.editProperties(modelMap);
			
			if(!changesConfirmed)
			{
				return false;
			}
			else if(allModelsAreValid(modelMap))
			{
				return true;
			}
			else 
			{
				_view.displayErrorMessage("Fix invalid properties!!");
			}
		}
	}

	private boolean allModelsAreValid(Map<String, PropertyEditorTableModel> modelMap)
	{
		for (PropertyEditorTableModel tm : modelMap.values())
		{
			if(!tm.isValid()) return false;
		}
		return true;
	}
}
