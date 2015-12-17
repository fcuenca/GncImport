package gncimport.ui;

import gncimport.interactors.AccFileLoadInteractor;

import java.io.File;

public class AccFileLoadPresenter implements  AccFileLoadInteractor.OutPort
{
	private TxView _view;
	private UIConfig _config;

	public AccFileLoadPresenter(TxView view, UIConfig config)
	{
		this._view = view;
		this._config = config;
	}

	@Override
	public void fileWasOpened(String fileName)
	{
		_view.updateGncFileLabel(fileName);
		_config.setLastGncDirectory(new File(fileName).getParent());						
	}		
}