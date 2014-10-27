package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import gncimport.models.AccountData;
import gncimport.models.MonthlyAccountParam;
import gncimport.tests.data.TestDataConfig;
import gncimport.tests.data.TestFiles;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

public class LocalModelCreatesNewAccountHierarchies
{
	private LocalFileTxImportModel_ForTesting _model;
	private AccountData _parent;

	@Before
	public void setUp()
	{
		_model = new LocalFileTxImportModel_ForTesting(TestDataConfig.DEFAULT_TARGET_ACCOUNT);

		_model.openGncFile(TestFiles.GNC_TEST_FILE);
		_parent = _model.findAccountByName("Year 2014");	
	}
	
	@Test
	public void new_hierarchy_is_added_to_the_gnc_file()
	{
		ArrayList<MonthlyAccountParam> subAccounts = new ArrayList<MonthlyAccountParam>();
		subAccounts.add(new MonthlyAccountParam(1, "New Gastos Varios"));
		subAccounts.add(new MonthlyAccountParam(2, "New Entertainment"));
		subAccounts.add(new MonthlyAccountParam(3, "New Auto"));

		_model.createNewAccountHierarchy(_parent, "This Month", subAccounts, "filename.gnc");

		AccountData root = _model.findAccountByName("This Month");
		
		assertThat(root, is(notNullValue()));
		assertThat(root.getParentId(), is(_parent.getId()));
		
		assertSubaccountsEqual(root, subAccounts);
	}

	private void assertSubaccountsEqual(AccountData root, ArrayList<MonthlyAccountParam> expectedSubAccounts)
	{
		for (MonthlyAccountParam p : expectedSubAccounts)
		{
			AccountData acc = _model.findAccountByName(p.accName);
			
			assertThat(p.accName + " not found", acc, is(notNullValue()));
			assertThat(p.accName + " has wrong parent: " + acc.getParentId(), acc.getParentId(), is(root.getId()));
		}
	}

	@Test
	public void saves_new_accounts_to_file()
	{	
		_model.createNewAccountHierarchy(_parent, "This Month", new ArrayList<MonthlyAccountParam>(), "filename.gnc");
		
		assertThat(_model.detectedFileName, is("filename.gnc"));	
	}
}
