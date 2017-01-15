package gncimport.ui.presenters;

import gncimport.interactors.PropertyEditInteractor;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleCategory;
import gncimport.transfer.RuleTester;
import gncimport.ui.TxView;
import gncimport.ui.swing.AccOverrideRulesTableModel;
import gncimport.ui.swing.IgnoreRulesTableModel;
import gncimport.ui.swing.TxRuleTableModel;
import gncimport.ui.swing.TxOverrideRuleTableModel;

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

	@SuppressWarnings("unchecked")
	@Override
	public boolean editProperties(Map<RuleCategory, Object> allRules, RuleTester tester)
	{	
		List<MatchingRule> ignoreRules = (List<MatchingRule>)allRules.get(RuleCategory.ignore);
		List<OverrideRule> accOverrideRules = (List<OverrideRule>)allRules.get(RuleCategory.acc_override);
		List<OverrideRule> txOverrideRules = (List<OverrideRule>)allRules.get(RuleCategory.tx_override);
		
		Map<RuleCategory, TxRuleTableModel> modelMap = new HashMap<RuleCategory, TxRuleTableModel>();
		modelMap.put(RuleCategory.ignore, new IgnoreRulesTableModel(ignoreRules, tester)); //TODO: connection b/w table model and tester is not really tested
		modelMap.put(RuleCategory.acc_override, new AccOverrideRulesTableModel(accOverrideRules, tester));
		modelMap.put(RuleCategory.tx_override, new TxOverrideRuleTableModel(txOverrideRules, tester));
		
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

	private boolean allModelsAreValid(Map<RuleCategory, TxRuleTableModel> modelMap) //TODO: pass values collection -- whole map not needed
	{
		for (TxRuleTableModel tm : modelMap.values())
		{
			if(!tm.isValid()) return false;
		}
		return true;
	}
}
