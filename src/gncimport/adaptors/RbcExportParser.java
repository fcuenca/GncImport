package gncimport.adaptors;

import gncimport.transfer.TxData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

public class RbcExportParser
{

	private final Reader _txFileReader;

	public RbcExportParser(Reader reader)
	{
		this._txFileReader = reader;
	}

	public RbcExportParser(String fileName) throws FileNotFoundException
	{
		this(new FileReader(fileName));
	}

	public List<TxData> getTransactions() throws IOException
	{
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

		ArrayList<TxData> result = new ArrayList<TxData>();

		CsvListReader csv = new CsvListReader(_txFileReader, CsvPreference.STANDARD_PREFERENCE);

		try
		{
			csv.getHeader(true); // skip past the header
	
			final CellProcessor[] processors = new CellProcessor[] {
					null, // [0] Account Type
					null, // [1] Account Number
					new NotNull(), // [2] Date
					null, // [3] Cheque Number
					new ConvertNullTo(""), // [4] Description 1
					new ConvertNullTo(""), // [5] Description 2
					new NotNull(), // [6] CAD$
					null, // [7] USD$
					null }; // [8] n/a
	
			while (csv.read() != null)
			{
				if (csv.length() > 1)
				{
					List<Object> values = csv.executeProcessors(processors);
					try
					{
						result.add(new TxData(
								dateFormatter.parse(values.get(2).toString()),
								new BigDecimal(values.get(6).toString()),
								values.get(4) + " - " + values.get(5)));
					}
					catch (ParseException e)
					{
						throw new RuntimeException("Date cannot be parsed: " + values.get(2).toString(), e);
					}
				}
			}
		}
		finally
		{
			csv.close();
		}
		
		return result;
	}
}
