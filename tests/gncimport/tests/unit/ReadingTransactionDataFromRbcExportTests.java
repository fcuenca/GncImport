package gncimport.tests.unit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import gncimport.adaptors.RbcExportParser;
import gncimport.tests.data.TestFiles;
import gncimport.transfer.TxData;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class ReadingTransactionDataFromRbcExportTests
{
	private static final String FILE_HEADER = "\"Account Type\",\"Account Number\",\"Transaction Date\",\"Cheque Number\",\"Description 1\",\"Description 2\",\"CAD$\",\"USD$\"\n";

	@Test
	public void can_handle_file_with_no_transactions()
	{
		try
		{
			StringReader reader = new StringReader(FILE_HEADER);

			RbcExportParser parser = new RbcExportParser(reader);

			List<TxData> txs;
			txs = parser.getTransactions();

			assertThat(txs.size(), is(0));
		}
		catch (IOException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void maps_rows_to_transactions()
	{
		try
		{
			StringReader reader = new StringReader(
					FILE_HEADER +
							"Chequing,123456789,1/2/2014,,\"MISC PAYMENT\",\"IMH POOL I LP \",-1635.00,,\n");

			RbcExportParser parser = new RbcExportParser(reader);

			List<TxData> txs = parser.getTransactions();

			@SuppressWarnings("deprecation")
			TxData expected = new TxData(new Date(2014 - 1900, 0, 2), new BigDecimal("-1635.00"),
					"MISC PAYMENT - IMH POOL I LP ");

			assertThat(txs.get(0), is(expected));
		}
		catch (IOException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void sign_is_preserved_during_extraction()
	{
		try
		{
			StringReader reader = new StringReader(
					FILE_HEADER +
							"Chequing,123456789,1/2/2014,,EXPENSE,,-1635.00,,\n" +
							"Chequing,123456789,1/2/2014,,REFUND,,33.25,,\n");

			RbcExportParser parser = new RbcExportParser(reader);

			List<TxData> txs = parser.getTransactions();

			assertThat(txs.get(0).amount, is(new BigDecimal("-1635.00")));
			assertThat(txs.get(1).amount, is(new BigDecimal("33.25")));
		}
		catch (IOException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void loads_all_transactions_in_file()
	{
		try
		{
			StringReader reader = new StringReader(
					FILE_HEADER +
							"Chequing,123456789,1/2/2014,,\"MISC PAYMENT\",\"IMH POOL I LP \",-1635.00,,\n" +
							"Chequing,123456789,1/15/2014,000135,\"CHEQUE - # 135\",,-250.00,,\n" +
							"Chequing,123456789,1/10/2014,,\"Withdrawal\",\"PTB WD --- TZ444087 \",-100.00,,\n");

			RbcExportParser parser = new RbcExportParser(reader);

			List<TxData> txs = parser.getTransactions();

			assertThat(txs.size(), is(3));
		}
		catch (IOException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void ignores_final_null_line_if_present()
	{
		try
		{
			StringReader reader = new StringReader(
					FILE_HEADER +
							"\0\n");

			RbcExportParser parser = new RbcExportParser(reader);

			List<TxData> txs;
			txs = parser.getTransactions();

			assertThat(txs.size(), is(0));
		}
		catch (IOException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void can_read_from_filesystem()
	{
		try
		{
			RbcExportParser parser = new RbcExportParser(TestFiles.CSV_1_TEST_FILE);

			List<TxData> txs;
			txs = parser.getTransactions();

			assertThat(txs.size(), is(20));
		}
		catch (IOException e)
		{
			fail(e.getMessage());
		}
	}

}
