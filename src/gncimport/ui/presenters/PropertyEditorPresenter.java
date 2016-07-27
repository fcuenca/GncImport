package gncimport.ui.presenters;

import gncimport.interactors.PropertyEditInteractor;
import gncimport.transfer.MatchingRule;
import gncimport.transfer.OverrideRule;
import gncimport.transfer.RuleTester;
import gncimport.ui.TxView;
import gncimport.ui.swing.PropertyEditorTableModel;

import java.util.List;

public class PropertyEditorPresenter implements PropertyEditInteractor.OutPort
{
	private TxView _view;

	public PropertyEditorPresenter(TxView view)
	{
		this._view = view;
	}

	@Override
	public boolean editProperties(final List<MatchingRule> rules, List<OverrideRule> accountOverrideRules, RuleTester tester)
	{		
		PropertyEditorTableModel tableModel = new PropertyEditorTableModel(rules, tester);

		boolean changesConfirmed;
		
		while(true)
		{
			changesConfirmed = _view.editProperties(tableModel);
			
			if(!changesConfirmed)
			{
				return false;
			}
			else if(tableModel.isValid())
			{
				return true;
			}
			else 
			{
				_view.displayErrorMessage("Fix invalid properties!!");
			}
		}
	}
}
