package gncimport.ui;

import gncimport.boundaries.TxModel;
import gncimport.boundaries.TxView;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jdesktop.swingx.JXStatusBar;

@SuppressWarnings("serial")
public class GncImportMainWindow extends JPanel implements TxView
{
	private final MainWindowRenderer _presenter;
	private JLabel _statusLabel;
	private JTable _table;

	public GncImportMainWindow(TxModel model)
	{
		this._presenter = new MainWindowPresenter(model, this);
		initialize();
	}

	public GncImportMainWindow(MainWindowRenderer presenter)
	{
		this._presenter = presenter;
		initialize();
	}

	private void initialize()
	{
		setLayout(new BorderLayout());
		setOpaque(true);

		JScrollPane grid = createGridPane();
		add(grid, BorderLayout.CENTER);

		JXStatusBar statusBar = createStatusBar();
		add(statusBar, BorderLayout.PAGE_END);

		_presenter.onReadFromCsvFile(
				getClass().getResource("../tests/data/rbc.csv").getPath());
	}

	@Override
	public void displayTxCount(int count)
	{
		if (count < 0)
		{
			throw new IllegalArgumentException("Transaction count can't be negative");
		}
		String message = "" + count;

		if (count == 1)
		{
			message += " transaction.";
		}
		else
		{
			message += " transactions.";
		}

		_statusLabel.setText(message);
	}

	private JScrollPane createGridPane()
	{
		_table = new JTable();
		_table.setName("TRANSACTION_GRID");

		return new JScrollPane(_table);
	}

	private JXStatusBar createStatusBar()
	{
		JXStatusBar sb = new JXStatusBar();

		_statusLabel = new JLabel();
		_statusLabel.setName("TX_COUNT");

		sb.add(_statusLabel);

		return sb;
	}

	@Override
	public void displayTxData(TxTableModel tableModel)
	{
		_table.setModel(tableModel);
	}

	@Override
	public TxTableModel getTxTableModel()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
