package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import gncimport.models.GncXmlLibUtils;
import gncimport.tests.data.TestDataConfig;
import gncimport.tests.data.TestFiles;
import gncimport.transfer.Month;
import gncimport.transfer.MonthlyAccount;

import java.util.ArrayList;

import org.gnucash.xml.gnc.Account;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

public class LocalModelCreatesNewAccountHierarchies
{
	private static final int SOME_MONTH = 5;
	private static final Month IRRELEVANT_MONTH = new Month(1);
	private LocalFileTxImportModel_ForTesting _model;
	private Account _parent;

	@Before
	public void setUp()
	{
		_model = new LocalFileTxImportModel_ForTesting(TestDataConfig.DEFAULT_TARGET_ACCOUNT);

		_model.openGncFile(TestFiles.GNC_TEST_FILE);
		_parent = _model.findAccountById(TestFiles.YEAR2014_ID);	
	}
	
	@Test
	public void new_hierarchy_is_added_to_the_gnc_file()
	{
		ArrayList<MonthlyAccount> subAccounts = new ArrayList<MonthlyAccount>();
		subAccounts.add(new MonthlyAccount(1, "New Gastos Varios"));
		subAccounts.add(new MonthlyAccount(2, "New Entertainment"));
		subAccounts.add(new MonthlyAccount(3, "New Auto"));

		Month month = new Month(SOME_MONTH);
		
		_model.createNewAccountHierarchy(GncXmlLibUtils.fromAccount(_parent), "This Month", month, subAccounts, "filename.gnc");

		Account root = _model.findAccountByName("This Month");
		
		assertAccountWasCorrectlyLinked(root, _parent, "42014", month, 0);
		assertEachSubAccountWasCorrectlyLinked(subAccounts, root, "42014", month);
	}

	private void assertAccountWasCorrectlyLinked(Account account, Account parent, String prefix, Month month, int sequenceNo)
	{
		assertThat("account wasn't found (seq=" + sequenceNo + ")", account, is(notNullValue()));
		assertThat("account should point to its parent", account.getParent().getValue(), is(parent.getId().getValue()));
		assertThat("account's code should be based on parent, with month number", account.getCode(), is(validAccountCode(prefix, month, sequenceNo)));
	}

	private void assertEachSubAccountWasCorrectlyLinked(ArrayList<MonthlyAccount> expectedSubAccounts, Account root, String prefix, Month month)
	{
		for (MonthlyAccount p : expectedSubAccounts)
		{
			Account acc = _model.findAccountByName(p.getAccName());
			
			assertAccountWasCorrectlyLinked(acc, root, prefix, month, p.sequenceNo);
		}
	}

	private Matcher<String> validAccountCode(final String prefix, final Month month, final int sequenceNo)
	{
		return new TypeSafeMatcher<String>()
		{
			private String expected = prefix + month.toNumericString() + String.format("%02d", sequenceNo);;

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
		_model.createNewAccountHierarchy(GncXmlLibUtils.fromAccount(_parent), "This Month", IRRELEVANT_MONTH, new ArrayList<MonthlyAccount>(), "filename.gnc");
		
		assertThat(_model.detectedFileName, is("filename.gnc"));	
	}
}
