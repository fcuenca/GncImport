package gncimport.interactors;

import java.util.ArrayList;
import java.util.List;

import gncimport.models.PropertyModel;

public class PropertyEditInteractor
{
	private OutPort _outPort;
	private PropertyModel _model;

	public interface OutPort
	{
		void editProperties(List<String> ignoreRules);
	}
	
	public PropertyEditInteractor(OutPort outPort, PropertyModel model)
	{
		this._outPort = outPort;
		this._model = model;
	}

	public void editProperties()
	{
		List<String> ignoreRules = new ArrayList<String>();
		
		_model.copyIgnoreRules(ignoreRules);
				
		_outPort.editProperties(ignoreRules);
		
		_model.replaceIgnoreRules(ignoreRules);
		
	}
}