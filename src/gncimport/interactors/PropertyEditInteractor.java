package gncimport.interactors;

import gncimport.models.RuleModel;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;

import java.util.HashMap;
import java.util.Map;

public class PropertyEditInteractor implements RuleTester 
{
	private OutPort _outPort;
	private RuleModel _model;

	public interface OutPort
	{
		boolean editProperties(Map<String, Object> allRules, RuleTester tester);
	}
	
	public PropertyEditInteractor(OutPort outPort, RuleModel model)
	{
		this._outPort = outPort;
		this._model = model;
	}

	public void editProperties()
	{
		Map<String, Object> allRules = new HashMap<String, Object>();
				
		_model.copyRulesTo(allRules);
		
		if(_outPort.editProperties(allRules, this))
		{
			_model.replaceRulesWith(allRules);
		}		
	}
	
	@Override
	public boolean tryMatchingRulesWithText(String textToMatch, Iterable<MatchingRule> candidateRules)
	{
		return _model.testMatchingRulesWithText(textToMatch, candidateRules);
	}

	@Override
	public String tryOverrideRulesWithText(String textToMatch, Iterable<OverrideRule> candidateRules)
	{
		return _model.testOverrideRulesWithText(textToMatch, candidateRules);
	}

}