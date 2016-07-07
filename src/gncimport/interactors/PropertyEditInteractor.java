package gncimport.interactors;

import gncimport.models.RuleModel;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.RuleTester;

import java.util.ArrayList;
import java.util.List;

public class PropertyEditInteractor implements RuleTester 
{
	private OutPort _outPort;
	private RuleModel _model;

	public interface OutPort
	{
		boolean editProperties(List<MatchingRule> ignoreRules, RuleTester tester);
	}
	
	public PropertyEditInteractor(OutPort outPort, RuleModel model)
	{
		this._outPort = outPort;
		this._model = model;
	}

	public void editProperties()
	{
		List<MatchingRule> ignoreRules = new ArrayList<MatchingRule>();
		
		_model.copyIgnoreRules(ignoreRules);

		if(_outPort.editProperties(ignoreRules, this))
		{
			_model.replaceIgnoreRules(ignoreRules);
		}		
	}
	
	@Override
	public boolean tryRulesWithText(String textToMatch, Iterable<MatchingRule> candidateRules)
	{
		return _model.testRulesWithText(textToMatch, candidateRules);
	}

}