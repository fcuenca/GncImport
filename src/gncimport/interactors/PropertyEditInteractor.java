package gncimport.interactors;

import gncimport.models.RuleModel;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyEditInteractor implements RuleTester 
{
	private OutPort _outPort;
	private RuleModel _model;

	public interface OutPort
	{
		boolean editProperties(List<MatchingRule> ignoreRules, List<OverrideRule> accountOverrideRules, Map<String, Object> allRules, RuleTester tester);
	}
	
	public PropertyEditInteractor(OutPort outPort, RuleModel model)
	{
		this._outPort = outPort;
		this._model = model;
	}

	public void editProperties()
	{
		List<MatchingRule> ignoreRules = new ArrayList<MatchingRule>();
		Map<String, Object> allRules = new HashMap<String, Object>();
		
		_model.copyRulesTo(ignoreRules, allRules);

		if(_outPort.editProperties(ignoreRules, new ArrayList<OverrideRule>(), allRules, this))
		{
			_model.replaceRulesWith(ignoreRules, allRules);
		}		
	}
	
	@Override
	public boolean tryRulesWithText(String textToMatch, Iterable<MatchingRule> candidateRules)
	{
		return _model.testRulesWithText(textToMatch, candidateRules);
	}

}