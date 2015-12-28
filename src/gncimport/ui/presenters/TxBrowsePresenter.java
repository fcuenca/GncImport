package gncimport.ui.presenters;

import gncimport.interactors.TxBrowseInteractor;
import gncimport.transfer.AccountData;
import gncimport.transfer.TxData;
import gncimport.ui.CandidateAccList;
import gncimport.ui.TxView;
import gncimport.ui.UIConfig;
import gncimport.ui.swing.TxTableModel;

import java.io.File;
import java.util.List;

public class TxBrowsePresenter implements TxBrowseInteractor.OutPort
{
	private TxView _view;
	private UIConfig _config;

	public TxBrowsePresenter(TxView view, UIConfig config)
	{
		this._view = view;
		this._config = config;
	}

	@Override
	public void accept(List<TxData> txList, List<AccountData> theAccList)
	{
		_view.displayTxData(new TxTableModel(txList), CandidateAccList.build(theAccList));
		_view.displayTxCount(txList.size());
	}

	@Override
	public void fileWasOpened(String fileName)
	{
		_config.setLastCsvDirectory(new File(fileName).getParent());
		_view.updateCsvFileLabel(fileName);
	}
}