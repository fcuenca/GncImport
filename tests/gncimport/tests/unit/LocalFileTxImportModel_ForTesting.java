package gncimport.tests.unit;

import gncimport.models.AccountData;
import gncimport.models.LocalFileTxImportModel;
import gncimport.models.TxData;

import java.io.IOException;
import java.util.List;

/*
 * This class is used to intercept calls into the model at specific seams,
 * to avoid having to mock the GncFile class and at the same time
 * avoid overwriting the gnc test file.
 */
public class LocalFileTxImportModel_ForTesting extends LocalFileTxImportModel
{
	public LocalFileTxImportModel_ForTesting(String defaultTargetAccName)
	{
		super(defaultTargetAccName);
	}

	public String detectedFileName;
	public String detectedSourceAccId;
	public List<TxData> detectedTransactions;

	@Override
	protected void saveToGncFile(String fileName) throws IOException
	{
		detectedFileName = fileName;
	}

	@Override
	protected void addNewTransactions(List<TxData> transactions, String sourceAccId)
	{
		detectedTransactions = transactions;
		detectedSourceAccId = sourceAccId;
		super.addNewTransactions(transactions, sourceAccId);
	}

	public int getTxCount()
	{
		return _gnc.getTransactionCount();
	}
	
	public int getAccountCount()
	{
		return _gnc.getAccountCount();
	}
	
	public AccountData findAccountByName(String accName)
	{
		return AccountData.fromAccount(_gnc.findAccountByName(accName));
	}
}