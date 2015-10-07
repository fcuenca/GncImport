package gncimport.specs.steps.hypodermic;

import gncimport.ConfigOptions;
import gncimport.GncImportApp;
import gncimport.adaptors.RbcExportParser;
import gncimport.models.TxData;
import gncimport.models.TxImportModel;
import gncimport.tests.data.TestFiles;
import gncimport.ui.MainWindowPresenter;
import gncimport.ui.TxTableModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class HypodermicAppDriver
{
	private FakeView _view;		
	private TxImportModel _model;
	private ConfigOptions _config;
	
	private MainWindowPresenter _presenter;
	
	public HypodermicAppDriver()
	{
		_config = new ConfigOptions(new Properties());
		
		GncImportApp.DEFAULT_TARGET_ACCOUNT = "Miscelaneous";
		
		_model = GncImportApp.createAppModel(_config);
		_view = new FakeView();

		_presenter = new MainWindowPresenter(_model, _view, _config);
	}
	
	public void openCsvFile(String fileName)
	{
		_view.FileNameToOpen = TestFiles.getFilePath(fileName);
		_presenter.onReadFromCsvFile();
	}
	
	public void openGncFile(String fileName)
	{	
		_view.FileNameToOpen = TestFiles.getFilePath(fileName);
		_presenter.onLoadGncFile();
	}

	public int observedTxCount()
	{
		return _view.TxCount;
	}
	
	public String loadedCvsFile()
	{
		return _view.FileNameToOpen;
	}
	
	public int observedGridSize() throws Exception
	{
		return _view.TableModel.getRowCount();
	}

	public String observedTxAtRow(int i)
	{
		return _view.TableModel.getValueAt(i, TxTableModel.DESCRIPTION_COLUMN).toString();
	}

	public String observedAccountAtRow(int i)
	{
		return _view.TableModel.getValueAt(i, TxTableModel.ACCOUNT_COLUMN).toString();
	}

	public void selectTargetAccHierarchy(String accountName)
	{
		_view.AccountToSelect = accountName;
		_presenter.onSelectTargetHierarchy();
	}

}