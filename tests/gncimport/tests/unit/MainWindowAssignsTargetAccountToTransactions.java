package gncimport.tests.unit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import gncimport.models.TxData;
import gncimport.ui.GncImportMainWindow;
import gncimport.ui.TxTableModel;

import org.fest.swing.edt.GuiActionRunner;
import org.junit.Test;

public class MainWindowAssignsTargetAccountToTransactions extends MainWindowTests
{
	@Test
	public void assigns_default_target_account_to_all_transactions()
	{
		GuiActionRunner.execute(new ViewDriver()
		{
			protected void doActionsOnView(GncImportMainWindow v)
			{
				v.displayTxData(new TxTableModel(SampleTxData.txDataList()));

				TxTableModel txTableModel = v.getTxTableModel();

				assertThat(txTableModel, is(notNullValue()));

				for (TxData txData : txTableModel.getTransactions())
				{
					assertThat(txData.toString(), txData.targetAccoundId, is("e31486ad3b2c6cdedccf135d13538b29"));
				}
			}
		});

	}
}
