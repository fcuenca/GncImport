package gncimport.ui.presenters;

import gncimport.interactors.AccSelectionInteractor;
import gncimport.interactors.AccSelectionInteractor.NewHierarchyOpts;
import gncimport.models.AccountData;
import gncimport.ui.CandidateAccList;
import gncimport.ui.TxView;
import gncimport.ui.TxView.NewHierarchyParams;
import gncimport.utils.ProgrammerError;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class AccSelectionPresenter implements AccSelectionInteractor.OutPort
{
	private TxView _view;

	public AccSelectionPresenter(TxView view)
	{
		this._view = view;
	}

	@Override
	public AccountData selectAccount(List<AccountData> accounts)
	{
		DefaultMutableTreeNode selectedNode = _view.promptForAccount(getRootNode(accounts));
		
		if (selectedNode != null)
		{
			return (AccountData) selectedNode.getUserObject();
		}
					
		return null;
	}

	@Override
	public NewHierarchyOpts promptForNewHierarchy(List<AccountData> accounts)
	{
		NewHierarchyParams params = _view.promptForNewHierarchy(getRootNode(accounts));
		
		if (params != null)
		{
			if (params.parentNode == null || params.rootAccName == null || params.rootAccName.trim().isEmpty())
			{
				throw new ProgrammerError("Invalid values for new Hierarchy came through!!");
			}
							
			return new AccSelectionInteractor.NewHierarchyOpts((AccountData) params.parentNode.getUserObject(), params.rootAccName, params.month);
		}
		
		return null;
	}

	@Override
	public void targetHierarchyHasBeenSet(String accName, List<AccountData> candidateAccList)
	{
		_view.displayTargetHierarchy(accName);	
		_view.updateCandidateTargetAccountList(CandidateAccList.build(candidateAccList));
	}

	@Override
	public void sourceAccHasBeenSet(String accName)
	{
		_view.displaySourceAccount(accName);
	}	

	private DefaultMutableTreeNode getRootNode(List<AccountData> accounts)
	{
		AccountTreeBuilder builder = new AccountTreeBuilder();
		
		for (AccountData account : accounts)
		{
			builder.addNodeFor(account);
		}
		
		return builder.getRoot();
	}
}