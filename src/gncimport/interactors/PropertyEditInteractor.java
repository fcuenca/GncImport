package gncimport.interactors;

import gncimport.models.RuleModel;
import gncimport.transfer.RuleCategory;
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
		boolean editProperties(Map<RuleCategory, Object> allRules, RuleTester tester);
	}
	
	public PropertyEditInteractor(OutPort outPort, RuleModel model)
	{
		this._outPort = outPort;
		this._model = model;
	}

	public void editProperties()
	{
		Map<RuleCategory, Object> allRules = new HashMap<RuleCategory, Object>();
				
		_model.copyRulesTo(allRules);
		
		if(_outPort.editProperties(allRules, this))
		{
			_model.replaceRulesWith(allRules);
		}		
	}
	
	@Override
	public String tryRulesWithText(String textToMatch, Iterable<? extends TransactionRule> candidateRules)
	{
		return _model.testRulesWithText(textToMatch, candidateRules);
	}

}