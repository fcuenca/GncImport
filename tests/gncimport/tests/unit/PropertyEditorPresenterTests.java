package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gncimport.transfer.RuleDefinition;
import gncimport.ui.TxView;
import gncimport.ui.presenters.PropertyEditorPresenter;

import java.util.ArrayList;
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
		List<RuleDefinition> rules = new ArrayList<RuleDefinition>(ListUtils.list_of(new RuleDefinitionForTest("rule-1"), new RuleDefinitionForTest("rule-2")));
		
		when(_view.editProperties(expectedTableModel.capture())).thenReturn(true);
		
		assertThat(_presenter.editProperties(rules), is(true));
		
		TableModel tm = expectedTableModel.getValue();
		
		//TODO: extract class for this table model and test separately
		assertThat(tm.getRowCount(), is(2));
		assertThat(tm.getColumnCount(), is(1));
		assertThat(tm.getValueAt(1, 0), is((Object)"rule-2"));
		assertThat(tm.isCellEditable(0, 0), is(true));
		
		tm.setValueAt("new value", 1, 0);
		assertThat(tm.getValueAt(1, 0), is((Object)"new value"));
	}
	
	
	@Test
	public void signals_that_user_canceled_editing()
	{
		when(_view.editProperties(any(TableModel.class))).thenReturn(false);
		
		assertThat(_presenter.editProperties(new ArrayList<RuleDefinition>()), is(false));
	}


}
