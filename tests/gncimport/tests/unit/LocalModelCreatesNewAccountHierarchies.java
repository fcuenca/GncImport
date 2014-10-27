package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import gncimport.models.AccountData;
import gncimport.models.Month;
import gncimport.models.MonthlyAccountParam;
import gncimport.tests.data.TestDataConfig;
import gncimport.tests.data.TestFiles;

import java.util.ArrayList;

import org.gnucash.xml.gnc.Account;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

public class LocalModelCreatesNewAccountHierarchies
{
	private static final Month IRRELEVANT_MONTH = new Month(1);
	private LocalFileTxImportModel_ForTesting _model;
	private AccountData _parent;

	@Before
	public void setUp()
	{
		_model = new LocalFileTxImportModel_ForTesting(TestDataConfig.DEFAULT_TARGET_ACCOUNT);

		_model.openGncFile(TestFiles.GNC_TEST_FILE);
		_parent = AccountData.fromAccount(_model.findAccountByName("Year 2014"));	
	}
	
	@Test
	public void new_hierarchy_is_added_to_the_gnc_file()
	{
		ArrayList<MonthlyAccountParam> subAccounts = new ArrayList<MonthlyAccountParam>();
		subAccounts.add(new MonthlyAccountParam(1, "New Gastos Varios"));
		subAccounts.add(new MonthlyAccountParam(2, "New Entertainment"));
		subAccounts.add(new MonthlyAccountParam(3, "New Auto"));

		Month month = new Month(5);
		
		_model.createNewAccountHierarchy(_parent, "This Month", month, subAccounts, "filename.gnc");

		Account root = _model.findAccountByName("This Month");
		
		assertThat("root shouldn't be null", root, is(notNullValue()));
		assertThat("root should point to its parent", root.getParent().getValue(), is(_parent.getId()));
		assertThat("root's code should be based on parent, with month number", root.getCode(), is("420140500"));
		
		assertSubaccountsMatch(subAccounts, root, month);
	}

	private void assertSubaccountsMatch(ArrayList<MonthlyAccountParam> expectedSubAccounts, Account root, Month month)
	{
		for (MonthlyAccountParam p : expectedSubAccounts)
		{
			Account acc = _model.findAccountByName(p.accName);
			
			assertThat(p.accName + " not found", acc, is(notNullValue()));
			assertThat(p.accName + " has wrong parent: " + acc.getParent().getValue(), 
					acc.getParent().getValue(), is(root.getId().getValue()));
			
			assertThat(p.accName + " has wrong code: " + acc.getCode(), 
					acc.getCode(), is(validAccountCode(month, p.sequenceNo)));
		}
	}

	private Matcher<String> validAccountCode(final Month month, final int sequenceNo)
	{
		return new TypeSafeMatcher<String>()
		{
			private String expected = "42014" + month.toNumericString() + String.format("%02d", sequenceNo);;

			@Override
			public void describeTo(Description description)
			{
				description.appendText(expected);
			}

			@Override
			protected boolean matchesSafely(String item)
			{
				return item.matches(expected);
			}
		};
	}

	@Test
	public void saves_new_accounts_to_file()
	{	
		_model.createNewAccountHierarchy(_parent, "This Month", IRRELEVANT_MONTH, new ArrayList<MonthlyAccountParam>(), "filename.gnc");
		
		assertThat(_model.detectedFileName, is("filename.gnc"));	
	}
}
