package gncimport.ui;

import gncimport.boundaries.TxImportModel;
import gncimport.boundaries.TxView;
import gncimport.models.AccountData;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;

import javax.swing.BoxLayout;
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
public class GncImportMainWindow extends JPanel implements TxView
{

	private final MainWindowRenderer _presenter;
	private JLabel _statusLabel;
	private JTable _table;
	private JLabel _sourceAccLabel;
	private JLabel _targetAccLabel;

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

		add(createAccountBox(), BorderLayout.PAGE_START);
		add(createGridPane(), BorderLayout.CENTER);
		add(createStatusBar(), BorderLayout.PAGE_END);

		_presenter.onLoadGncFile(getClass().getResource("../tests/data/checkbook.xml").getPath());
		_presenter.onReadFromCsvFile(getClass().getResource("../tests/data/rbc.csv").getPath());
	}

	private JPanel createAccountBox()
	{
		_sourceAccLabel = new JLabel("Source Account: NOT SET");
		_targetAccLabel = new JLabel("Target Account: NOT SET");

		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
		box.add(_sourceAccLabel);
		box.add(_targetAccLabel);
		box.add(createSelectAccountButton());
		box.add(createTargetAccountButton());
		box.add(createImportButton());

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

	private JButton createImportButton()
	{
		JButton button = new JButton("Import");

		button.setName("SAVE_BUTTON");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onImportClick();
			}
		});

		return button;
	}

	private JButton createSelectAccountButton()
	{
		JButton button = new JButton("Select Source Account");

		button.setName("SELECT_SRC_ACC_BUTTON");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onSelectSourceAccClick();
			}
		});

		return button;
	}

	private JButton createTargetAccountButton()
	{
		JButton button = new JButton("Select Target Account");

		button.setName("SELECT_TARGET_ACC_BUTTON");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onSelectTargetAccClick();
			}
		});

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

	public void onSelectSourceAccClick()
	{
		_presenter.onSelectSourceAccount();
	}

	public void onImportClick()
	{
		_presenter.onSaveToGncFile("/tmp/checkbook-new.xml");
	}

	public void onSelectTargetAccClick()
	{
		_presenter.onSelectTargetAccount();
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
			// TODO: Spiked solution for intercepting the "OTHER" value
			// @Override
			// public Object getCellEditorValue()
			// {
			// AccountData value = (AccountData) super.getCellEditorValue();
			//
			// if (value.getName().equals("Expenses"))
			// {
			// ((JComboBox) getComponent()).hidePopup();
			// DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
			// root.add(new DefaultMutableTreeNode("acc 1"));
			// root.add(new DefaultMutableTreeNode("acc 2"));
			//
			// DefaultMutableTreeNode selected = displayAccountTree(root);
			// return new AccountData("Fer " +
			// selected.getUserObject().toString(), value.getId());
			// }
			// return value;
			// }

		};
		accountColumn.setCellEditor(editor);
	}
}
