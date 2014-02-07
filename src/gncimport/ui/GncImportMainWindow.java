package gncimport.ui;

import gncimport.boundaries.TxModel;
import gncimport.boundaries.TxView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jdesktop.swingx.JXStatusBar;

@SuppressWarnings("serial")
public class GncImportMainWindow extends JPanel implements TxView, ActionListener
{
	private final MainWindowRenderer _presenter;
	private JLabel _statusLabel;
	private JTable _table;
	JButton _saveButton;

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

		add(createGridPane(), BorderLayout.CENTER);
		add(createStatusBar(), BorderLayout.PAGE_END);
		add(createImportButton(), BorderLayout.PAGE_START);

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

	private JButton createImportButton()
	{
		JButton saveButton = new JButton("Import");

		saveButton.setName("SAVE_BUTTON");
		saveButton.addActionListener(this);

		return saveButton;
	}

	@Override
	public void displayTxData(TxTableModel tableModel)
	{
		_table.setModel(tableModel);
	}

	@Override
	public TxTableModel getTxTableModel()
	{
		return (TxTableModel) _table.getModel();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		_presenter.onSaveToGncFile(
				getClass().getResource("../tests/data/checkbook.xml").getPath() + ".new");
	}
}
