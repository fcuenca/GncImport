package gncimport.specs.steps.hypodermic;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.ConfigOptions;
import gncimport.GncImportApp;
import gncimport.models.TxImportModel;
import gncimport.tests.data.TestFiles;
import gncimport.tests.endtoend.FileSystemDriver;
import gncimport.ui.MainWindowPresenter;

import java.util.Properties;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


public class MainWindowSteps 
{
	private FileSystemDriver _fs;
	
	private MainWindowPresenter _presenter;
	private FakeView _view;
	private TxImportModel _model;
	private ConfigOptions _config;


	@Before
	public void beforeScenario()
	{
		_fs = new FileSystemDriver();
		_fs.prepareTestFiles();	
		
		_config = new ConfigOptions(new Properties());
		_model = GncImportApp.createAppModel(_config);
		_view = new FakeView();
		
		_presenter = new MainWindowPresenter(_model, _view, _config);
	}
	
	@After
	public void afterScenario()
	{
	}


	@Given("^the sample file \"([^\"]*)\" has been loaded$")
	public void the_sample_file_has_been_loaded(String fileName) throws Throwable 
	{
		_view.FileName = TestFiles.getFilePath(fileName);
		_presenter.onReadFromCsvFile();
	}

	@Then("^the status bar shows a count of (\\d+) transactions$")
	public void the_status_bar_shows_a_count_of_transactions(int txCount) throws Throwable 
	{
		assertThat(_view.TxCount, is(txCount));
	}

	@Then("^the transction grid shows (\\d+) rows$")
	public void the_transction_grid_shows_rows(int txCount) throws Throwable 
	{
		assertThat(_view.TableModel.getRowCount(), is(txCount));
	}

}
