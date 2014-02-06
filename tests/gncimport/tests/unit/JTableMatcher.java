package gncimport.tests.unit;

import static java.lang.String.valueOf;
import static org.fest.util.Strings.concat;

import javax.swing.JTable;

import org.fest.swing.annotation.RunsInCurrentThread;
import org.fest.swing.core.matcher.NamedComponentMatcherTemplate;

public class JTableMatcher extends NamedComponentMatcherTemplate<JTable>
{
	public static JTableMatcher withName(String name)
	{
		return new JTableMatcher(name);
	}

	public static JTableMatcher any()
	{
		return new JTableMatcher(ANY);
	}

	private JTableMatcher(Object name)
	{
		super(JTable.class, name);
	}

	public JTableMatcher andShowing()
	{
		requireShowing(true);
		return this;
	}

	@RunsInCurrentThread
	protected boolean isMatching(JTable table)
	{
		return isNameMatching(table.getName());
	}

	@Override
	public String toString()
	{
		return concat(
				getClass().getName(), "[",
				"name=", quotedName(), ", ",
				"requireShowing=", valueOf(requireShowing()),
				"]");
	}
}
