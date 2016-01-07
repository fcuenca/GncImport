package gncimport.interactors;

import gncimport.models.PropertyModel;
import gncimport.transfer.RuleDefinition;
import gncimport.transfer.UserEnteredRuleDefinition;

import java.util.ArrayList;
import java.util.List;

public class PropertyEditInteractor
{
	private OutPort _outPort;
	private PropertyModel _model;

	public interface OutPort
	{
		boolean editProperties(List<String> ignoreRules);
	}
	
	public PropertyEditInteractor(OutPort outPort, PropertyModel model)
	{
		this._outPort = outPort;
		this._model = model;
	}

	public void editProperties()
	{
		List<RuleDefinition> ignoreRules = new ArrayList<RuleDefinition>();
		
		_model.copyIgnoreRules(ignoreRules);
		
		List<String> rules = new ArrayList<String>();
		for (RuleDefinition r : ignoreRules)
		{
			rules.add(r.text());
		}
				
		if(_outPort.editProperties(rules))
		{
			ignoreRules.clear();
			for (String r : rules)
			{
				ignoreRules.add(new UserEnteredRuleDefinition(r));
			}
			_model.replaceIgnoreRules(ignoreRules);
		}
		
	}
}