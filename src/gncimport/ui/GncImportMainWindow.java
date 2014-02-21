package gncimport.ui;

import gncimport.boundaries.TxImportModel;
import gncimport.boundaries.TxView;
import gncimport.models.AccountData;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.error.ErrorInfo;

@SuppressWarnings("serial")
public class GncImportMainWindow extends JPanel implements TxView, ActionListener
{
	public static final String IMPORT_BUTTON = "IMPORT_BUTTON";
	public static final String SELECT_TARGET_ACC_BUTTON = "SELECT_TARGET_ACC_BUTTON";
	public static final String SELECT_SRC_ACC_BUTTON = "SELECT_SRC_ACC_BUTTON";
	public static final String OPEN_CSV_BUTTON = "OPEN_CSV_BUTTON";
	public static final String OPEN_GNC_BUTTON = "OPEN_GNC_BUTTON";

	private final MainWindowRenderer _presenter;

	private JLabel _statusLabel;
	private JTable _table;
	private JLabel _sourceAccLabel;
	private JLabel _targetAccLabel;
	private JLabel _gncFileLabel;
	private JLabel _csvFileLabel;

	public GncImportMainWindow(TxImportModel model)
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

		add(createControlBox(), BorderLayout.PAGE_START);
		add(createGridPane(), BorderLayout.CENTER);
		add(createStatusBar(), BorderLayout.PAGE_END);

	}

	private JPanel createControlBox()
	{
		_gncFileLabel = new JLabel("Select GNC File");
		_csvFileLabel = new JLabel("Select CSV File");

		_sourceAccLabel = new JLabel("Source Account: NOT SET");
		_targetAccLabel = new JLabel("Target Account: NOT SET");

		JPanel box = new JPanel();
		box.setLayout(new GridLayout(0, 2));

		box.add(_gncFileLabel);
		box.add(createButton(OPEN_GNC_BUTTON, "..."));

		box.add(_csvFileLabel);
		box.add(createButton(OPEN_CSV_BUTTON, "..."));

		box.add(_sourceAccLabel);
		box.add(createButton(SELECT_SRC_ACC_BUTTON, "..."));

		box.add(_targetAccLabel);
		box.add(createButton(SELECT_TARGET_ACC_BUTTON, "..."));

		box.add(createButton(IMPORT_BUTTON, "Import"));

		return box;
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

	protected JButton createButton(String actionCommand, String label)
	{
		JButton button = new JButton(label);

		button.setActionCommand(actionCommand);
		button.setName(actionCommand);
		button.addActionListener(this);

		return button;
	}

	@Override
	public void displayTxData(TxTableModel tableModel, List<AccountData> targetAccounts)
	{
		_table.setModel(tableModel);

		updateCandidateTargetAccountList(targetAccounts);
	}

	@Override
	public TxTableModel getTxTableModel()
	{
		return (TxTableModel) _table.getModel();
	}

	@Override
	public void handleException(Exception e)
	{
		String stackTrace = "";

		for (StackTraceElement se : e.getStackTrace())
		{
			stackTrace += se.toString() + "\n";
		}

		JXErrorPane.showDialog(this,
				new ErrorInfo("Critical Error",
						e.getMessage(),
						stackTrace,
						null,
						e, Level.SEVERE, null));
	}

	@Override
	public void displaySourceAccount(String accountName)
	{
		_sourceAccLabel.setText("Source Account: " + accountName);
	}

	@Override
	public void displayTargetHierarchy(String accountName)
	{
		_targetAccLabel.setText("Target Account: " + accountName);
	}

	@Override
	public DefaultMutableTreeNode displayAccountTree(DefaultMutableTreeNode rootNode)
	{
		AccountTreeBrowserDialog dlg = new AccountTreeBrowserDialog(null, "Source Account", rootNode);

		dlg.setVisible(true);

		return (DefaultMutableTreeNode) dlg.getSelectedNode();
	}

	@Override
	public void updateCandidateTargetAccountList(List<AccountData> accountList)
	{
		TableColumn accountColumn = _table.getColumnModel().getColumn(TxTableModel.ACCOUNT_COLUMN);

		JComboBox comboBox = new JComboBox(accountList.toArray());

		DefaultCellEditor editor = new DefaultCellEditor(comboBox)
		{
			private AccountData originalValue;

			@Override
			public Component getTableCellEditorComponent(JTable table, Object
					value, boolean isSelected, int row, int column)
			{
				originalValue = getTxTableModel().getTransactions().get(row).targetAccount;

				((JComboBox) getComponent()).setSelectedItem(originalValue);

				return super.getTableCellEditorComponent(table, value,
						isSelected, row, column);
			}

			@Override
			public Object getCellEditorValue()
			{
				AccountData newValue = (AccountData) super.getCellEditorValue();

				((JComboBox) getComponent()).hidePopup();

				return _presenter.onTargetAccountSelected(newValue, originalValue);
			}
		};

		accountColumn.setCellEditor(editor);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(OPEN_GNC_BUTTON))
		{
			String fileName = getClass().getResource("../tests/data/checkbook.xml").getPath();
			onLoadGncFile(fileName);
			_gncFileLabel.setText(fileName);
		}
		else if (e.getActionCommand().equals(OPEN_CSV_BUTTON))
		{
			String fileName = getClass().getResource("../tests/data/rbc.csv").getPath();
			onLoadCsvFile(fileName);
			_csvFileLabel.setText(fileName);
		}
		else if (e.getActionCommand().equals(IMPORT_BUTTON))
		{
			onImportClick();
		}
		else if (e.getActionCommand().equals(SELECT_SRC_ACC_BUTTON))
		{
			onSelectSourceAccClick();
		}
		else if (e.getActionCommand().equals(SELECT_TARGET_ACC_BUTTON))
		{
			onSelectTargetHierarchyClick();
		}

	}

	public void onSelectSourceAccClick()
	{
		_presenter.onSelectSourceAccount();
	}

	public void onImportClick()
	{
		_presenter.onSaveToGncFile("/tmp/checkbook-new.xml");
	}

	public void onSelectTargetHierarchyClick()
	{
		_presenter.onSelectTargetHierarchy();
	}

	public void onLoadCsvFile(String fileName)
	{
		_presenter.onReadFromCsvFile(fileName);
	}

	public void onLoadGncFile(String fileName)
	{
		_presenter.onLoadGncFile(fileName);
	}
}
