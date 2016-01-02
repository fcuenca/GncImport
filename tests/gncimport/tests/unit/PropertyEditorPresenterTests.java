package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gncimport.ui.TxView;
import gncimport.ui.presenters.PropertyEditorPresenter;

import java.util.List;

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
	private ArgumentCaptor<List<String>> expectedRules;

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
		
		verify(_view).editProperties(expectedRules.capture());

		assertThat(expectedRules.getValue(), is(rules));
	}

}
