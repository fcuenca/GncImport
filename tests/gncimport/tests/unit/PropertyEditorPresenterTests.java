package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.ui.TxView;
import gncimport.ui.presenters.PropertyEditorPresenter;

import java.util.List;

import javax.swing.table.TableModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PropertyEditorPresenterTests
{
	@Captor
	private ArgumentCaptor<TableModel> expectedTableModel;
	
	private TxView _view;
	private PropertyEditorPresenter _presenter;

	@Before
	public void Setup()
	{
		_view = mock(TxView.class);
		_presenter = new PropertyEditorPresenter(_view);
	}
	
	@Test
	public void displays_ignore_rules()
	{
		List<String> rules = ListUtils.list_of("rule-1", "rule-2");
		
		_presenter.editProperties(rules);
		
		verify(_view).editProperties(expectedTableModel.capture());
		
		TableModel tm = expectedTableModel.getValue();
		
		//TODO: extract class for this table model and test separately
		assertThat(tm.getRowCount(), is(2));
		assertThat(tm.getColumnCount(), is(1));
		assertThat(tm.getValueAt(1, 0), is((Object)"rule-2"));
		assertThat(tm.isCellEditable(0, 0), is(true));
		
		tm.setValueAt("new value", 1, 0);
		assertThat(tm.getValueAt(1, 0), is((Object)"new value"));

	}

}
