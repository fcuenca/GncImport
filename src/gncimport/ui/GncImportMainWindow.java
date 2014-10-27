package gncimport.ui;

import gncimport.GncImportApp;
import gncimport.models.AccountData;
import gncimport.models.TxImportModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXStatusBar;

@SuppressWarnings("serial")
public class GncImportMainWindow extends JPanel implements TxView, ActionListener
{
	public static final String IMPORT_BUTTON = "IMPORT_BUTTON";
	public static final String SELECT_TARGET_ACC_BUTTON = "SELECT_TARGET_ACC_BUTTON";
	public static final String SELECT_SRC_ACC_BUTTON = "SELECT_SRC_ACC_BUTTON";
	public static final String OPEN_CSV_BUTTON = "OPEN_CSV_BUTTON";
	public static final String OPEN_GNC_BUTTON = "OPEN_GNC_BUTTON";
	public static final String NEW_ACC_HIERARCHY_MENU = "NEW_ACC_HIERARCHY";

	private final MainWindowRenderer _presenter;
	
	private String _gncFileName = "";

	private JLabel _statusLabel;
	private JTable _table;
	private JLabel _sourceAccLabel;
	private JLabel _targetAccLabel;
	private JLabel _gncFileLabel;
	private JLabel _csvFileLabel;
	private JComboBox _candidateTargetAccComboBox = new JComboBox();
	private JXDatePicker _fromDatePicker;
	private JXDatePicker _toDatePicker;

	public GncImportMainWindow(TxImportModel model, UIConfig config)
	{
		this._presenter = new MainWindowPresenter(model, this, config);
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
		add(createBottomBox(), BorderLayout.PAGE_END);

	}

	private JPanel createControlBox()
	{
		_gncFileLabel = new JLabel("Select GNC File");
		_csvFileLabel = new JLabel("Select CSV File");

		_sourceAccLabel = new JLabel("Source Account: NOT SET");
		_targetAccLabel = new JLabel("Target Account: NOT SET");

		_toDatePicker = new JXDatePicker();
		_fromDatePicker = new JXDatePicker();

		JButton openGncBtn = createButton(OPEN_GNC_BUTTON, "Open");
		JButton openCsvBtn = createButton(OPEN_CSV_BUTTON, "Open");
		JButton selectSrcBtn = createButton(SELECT_SRC_ACC_BUTTON, "Select");
		JButton selectTargetBtn = createButton(SELECT_TARGET_ACC_BUTTON, "Select");

		JPanel box = new JPanel();
		box.setBorder(new EmptyBorder(5, 5, 5, 5));
		box.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 1.0;
		box.add(_gncFileLabel, c);

		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0;
		box.add(openGncBtn, c);

		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 1.0;
		box.add(_sourceAccLabel, c);

		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0;
		box.add(selectSrcBtn, c);

		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 1.0;
		box.add(_targetAccLabel, c);

		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0;
		box.add(selectTargetBtn, c);

		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 1.0;
		box.add(_csvFileLabel, c);

		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0;
		box.add(openCsvBtn, c);

		JPanel dateBox = new JPanel(new FlowLayout());
		dateBox.add(new JLabel("From"));
		dateBox.add(_fromDatePicker);
		dateBox.add(new JLabel("To"));
		dateBox.add(_toDatePicker);

		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1.0;
		box.add(dateBox, c);

		c.gridx = 1;
		c.gridy = 4;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		box.add(createButton("FILTER", "Filter"), c);

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

	private JPanel createBottomBox()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel.add(createStatusBar());
		panel.add(createButton(IMPORT_BUTTON, "Import"));

		return panel;
	}

	protected JXStatusBar createStatusBar()
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

		DefaultCellEditor editor = new DefaultCellEditor(_candidateTargetAccComboBox)
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

		TableColumn accountColumn = _table.getColumnModel().getColumn(TxTableModel.ACCOUNT_COLUMN);
		accountColumn.setCellEditor(editor);
	}

	@Override
	public TxTableModel getTxTableModel()
	{
		return (TxTableModel) _table.getModel();
	}

	@Override
	public void handleException(Exception e)
	{
		GncImportApp.displayError(this, e);
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
	public DefaultMutableTreeNode promptForAccount(DefaultMutableTreeNode rootNode)
	{
		AccountTreeBrowserDialog dlg = new AccountTreeBrowserDialog(null, "Select Account", rootNode);

		dlg.setVisible(true);

		return (DefaultMutableTreeNode) dlg.getSelectedNode();
	}
	
	@Override
	public String promptForFile(String initialDirectory)
	{
		JFileChooser fc = new JFileChooser();
		fc.setName("FILE_CHOOSER");
		fc.setCurrentDirectory(new File(initialDirectory));

		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			return fc.getSelectedFile().getAbsolutePath();
		}

		return null;
	}

	@Override
	public NewHierarchyParams promptForNewHierarchy(DefaultMutableTreeNode rootNode)
	{		
		NewAccHierarchyDialog dlg = new NewAccHierarchyDialog(null, "New Account Hierarchy", rootNode);
		
		dlg.setVisible(true);	
		
		return dlg.getNewHierarchyParams();		
	}

	@Override
	public void updateCandidateTargetAccountList(List<AccountData> accountList)
	{
		_candidateTargetAccComboBox.setModel(new DefaultComboBoxModel(accountList.toArray()));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(OPEN_GNC_BUTTON))
		{
			onLoadGncFile();
		}
		else if (e.getActionCommand().equals(OPEN_CSV_BUTTON))
		{
			onLoadCsvFile();
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
		else if (e.getActionCommand().equals("FILTER"))
		{
			onFilterTxList();
		}
		else if(e.getActionCommand().equals(NEW_ACC_HIERARCHY_MENU))
		{
			onNewAccHierarchy();
		}
	}

	@Override
	public void updateCsvFileLabel(String fileName)
	{
		_csvFileLabel.setText(fileName);
		_csvFileLabel.setToolTipText(fileName);
	}

	@Override
	public void updateGncFileLabel(String fileName)
	{
		_gncFileName = fileName;
		_gncFileLabel.setText(fileName);
		_gncFileLabel.setToolTipText(fileName);
	}

	public void onSelectSourceAccClick()
	{
		_presenter.onSelectSourceAccount();
	}

	public void onImportClick()
	{
		_presenter.onSaveToGncFile(_gncFileName);
	}

	public void onSelectTargetHierarchyClick()
	{
		_presenter.onSelectTargetHierarchy();
	}
	
	public void onNewAccHierarchy()
	{
		_presenter.onCreateNewAccHierarchy(_gncFileName);
	}

	public void onLoadCsvFile()
	{
		_presenter.onReadFromCsvFile();
	}

	public void onLoadGncFile()
	{
		_presenter.onLoadGncFile();
	}

	private void onFilterTxList()
	{
		_presenter.onFilterTxList(_fromDatePicker.getDate(), _toDatePicker.getDate());
	}

	@Override
	public void displayErrorMessage(String message)
	{
		JOptionPane.showMessageDialog(this, message);
	}
}
