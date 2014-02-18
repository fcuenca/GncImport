package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import gncimport.models.AccountData;
import gncimport.models.LocalFileTxImportModel;
import gncimport.models.TxData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gnucash.xml.gnc.Account;
import org.junit.Before;
import org.junit.Test;

public class LocalFileImportTests
{

	private class LocalFileTxImportModel_ForTesting extends LocalFileTxImportModel
	{

		public String detectedFileName;
		public String detectedSourceAccId;
		public List<TxData> detectedTransactions;

		@Override
		protected void saveToGncFile(String fileName, List<TxData> transactions, String sourceAccId) throws IOException
		{
			detectedFileName = fileName;
			detectedTransactions = transactions;
			detectedSourceAccId = sourceAccId;
		}
	}

	private LocalFileTxImportModel_ForTesting _model;

	@Before
	public void setUp()
	{
		_model = new LocalFileTxImportModel_ForTesting();
		_model.openGncFile(getClass().getResource("../data/checkbook.xml").getPath());
	}

	@Test
	public void can_provide_default_source_account()
	{
		AccountData account = _model.getDefaultSourceAccount();

		assertThat(account.getName(), is("Checking Account"));
		assertThat(account.getId(), is("64833494284bad5fb390e84d38c65a54"));
	}

	@Test
	public void can_provide_default_target_account()
	{
		AccountData account = _model.getDefaultTargetAccount();

		assertThat(account.getName(), is("Expenses"));
		assertThat(account.getId(), is("e31486ad3b2c6cdedccf135d13538b29"));
	}

	@Test
	public void can_provide_list_of_accounts()
	{
		List<Account> accounts = _model.getAccounts();

		assertThat(accounts.size(), is(8));
		assertThat(accounts.get(0).getName(), is("Root Account"));
		assertThat(accounts.get(7).getName(), is("Opening Balances"));
	}

	@Test
	public void can_save_to_new_file()
	{
		List<TxData> txList = new ArrayList<TxData>();

		_model.saveTxTo(txList, "new-file.xml");

		assertThat(_model.detectedFileName, is("new-file.xml"));
		assertThat(_model.detectedTransactions, is(txList));
		assertThat(_model.detectedSourceAccId, is("64833494284bad5fb390e84d38c65a54"));
	}

	@Test
	public void default_source_account_can_be_overriden()
	{
		List<TxData> txList = new ArrayList<TxData>();
		AccountData newAccount = new AccountData("new Account", "new-id");

		_model.setSourceAccount(newAccount);
		_model.saveTxTo(txList, "new-file.xml");

		assertThat(_model.detectedSourceAccId, is("new-id"));
	}
}
