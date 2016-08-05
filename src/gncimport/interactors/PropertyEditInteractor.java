package gncimport.interactors;

import gncimport.models.RuleModel;
import gncimport.transfer.RuleTester;
import gncimport.transfer.TransactionRule;

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
	public boolean tryMatchingRulesWithText(String textToMatch, Iterable<? extends TransactionRule> candidateRules)
	{
		return _model.testMatchingRulesWithText(textToMatch, candidateRules);
	}

	@Override
	public String tryOverrideRulesWithText(String textToMatch, Iterable<? extends TransactionRule> candidateRules)
	{
		return _model.testOverrideRulesWithText(textToMatch, candidateRules);
	}

}