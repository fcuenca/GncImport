package gncimport.interactors;

import gncimport.models.RuleModel;
import gncimport.transfer.RuleDefinition;

import java.util.ArrayList;
import java.util.List;

public class PropertyEditInteractor
{
	private OutPort _outPort;
	private RuleModel _model;

	public interface OutPort
	{
		boolean editProperties(List<RuleDefinition> ignoreRules);
	}
	
	public PropertyEditInteractor(OutPort outPort, RuleModel model)
	{
		this._outPort = outPort;
		this._model = model;
	}

	public void editProperties()
	{
		List<RuleDefinition> ignoreRules = new ArrayList<RuleDefinition>();
		
		_model.copyIgnoreRules(ignoreRules);

		if(_outPort.editProperties(ignoreRules))
		{
			_model.replaceIgnoreRules(ignoreRules);
		}		
	}
	
	//public boolean tryRulesWithText(String textToMatch, Iterable<RuleDefinition> candidateRules);
}