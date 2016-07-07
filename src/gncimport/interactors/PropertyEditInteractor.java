package gncimport.interactors;

import gncimport.models.RuleModel;
import gncimport.transfer.MatchingText;
import gncimport.transfer.RuleTester;

import java.util.ArrayList;
import java.util.List;

public class PropertyEditInteractor implements RuleTester 
{
	private OutPort _outPort;
	private RuleModel _model;

	public interface OutPort
	{
		boolean editProperties(List<MatchingText> ignoreRules, RuleTester tester);
	}
	
	public PropertyEditInteractor(OutPort outPort, RuleModel model)
	{
		this._outPort = outPort;
		this._model = model;
	}

	public void editProperties()
	{
		List<MatchingText> ignoreRules = new ArrayList<MatchingText>();
		
		_model.copyIgnoreRules(ignoreRules);

		if(_outPort.editProperties(ignoreRules, this))
		{
			_model.replaceIgnoreRules(ignoreRules);
		}		
	}
	
	@Override
	public boolean tryRulesWithText(String textToMatch, Iterable<MatchingText> candidateRules)
	{
		return _model.testRulesWithText(textToMatch, candidateRules);
	}

}