package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import gncimport.models.AccountData;
import gncimport.tests.data.TestDataConfig;
import gncimport.tests.data.TestFiles;

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
	public void new_accounts_are_added_to_the_gnc_file()
	{
		int initialCount = _model.getAccountCount();

		_model.createNewAccountHierarchy(_parent, "This Month", "filename.gnc");
		
		assertThat(_model.getAccounts(), containsAccount("This Month"));
		assertThat(_model.getAccountCount(), is(initialCount + 1)); 
	}
	
	@Test
	public void root_of_hierarchy_is_linked_to_parent_account()
	{
		_model.createNewAccountHierarchy(_parent, "This Month", "filename.gnc");

		AccountData root = _model.findAccountByName("This Month");
		
		assertThat(root, is(notNullValue()));
		assertThat(root.getParentId(), is(_parent.getId()));
	}

	@Test
	public void saves_new_accounts_to_file()
	{	
		_model.createNewAccountHierarchy(_parent, "This Month", "filename.gnc");
		
		assertThat(_model.detectedFileName, is("filename.gnc"));	
	}

	private Matcher<List<AccountData>> containsAccount(final String accName)
	{
		return new TypeSafeMatcher<List<AccountData>>()
		{			
			@Override
			public void describeTo(Description description)
			{
				description.appendText(accName);
			}

			@Override
			protected boolean matchesSafely(List<AccountData> accList)
			{
				for (AccountData acc : accList)
				{
					if (acc.getName().equals(accName))
					{
						return true;
					}
				}
				return false;
			}
		};
		
	}


}
