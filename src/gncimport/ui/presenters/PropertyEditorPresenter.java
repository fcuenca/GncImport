package gncimport.ui.presenters;

import gncimport.interactors.PropertyEditInteractor;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.RuleTester;
import gncimport.ui.TxView;
import gncimport.ui.swing.IgnoreRulesTableModel;
import gncimport.ui.swing.RuleTableModel;

import java.util.HashMap;
import java.util.List;
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
		@SuppressWarnings("unchecked")
		List<MatchingRule> ignoreRules = (List<MatchingRule>)allRules.get("ignore");
		
		Map<String, RuleTableModel> modelMap = new HashMap<String, RuleTableModel>();
		modelMap.put("ignore", new IgnoreRulesTableModel(ignoreRules, tester));
		
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

	private boolean allModelsAreValid(Map<String, RuleTableModel> modelMap)
	{
		for (RuleTableModel tm : modelMap.values())
		{
			if(!tm.isValid()) return false;
		}
		return true;
	}
}
