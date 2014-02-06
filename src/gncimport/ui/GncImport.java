package gncimport.ui;

import gncimport.boundaries.MainWindowRenderer;
import gncimport.boundaries.TxModel;
import gncimport.boundaries.TxView;
import gncimport.models.FakeTxModel;
import gncimport.presenters.MainWindowPresenter;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXStatusBar;

@SuppressWarnings("serial")
public class GncImport extends JPanel implements TxView
{
	private static JFrame _mainFrame;

	private final MainWindowRenderer _presenter;
	private JLabel _statusLabel;

	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI()
	{
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		JFrame frame = createMainFrame();
		frame.pack();
		frame.setVisible(true);
	}

	public static JFrame createMainFrame()
	{
		return createMainFrame(new FakeTxModel());
	}

	public static JFrame createMainFrame(TxModel model)
	{
		_mainFrame = new JFrame("GnuCash Transaction Import");

		_mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GncImport newContentPane = new GncImport(model);

		_mainFrame.setContentPane(newContentPane);

		return _mainFrame;
	}

	public GncImport(TxModel model)
	{
		this._presenter = new MainWindowPresenter(model, this);
		initialize();
	}

	public GncImport(MainWindowRenderer presenter)
	{
		this._presenter = presenter;
		initialize();
	}

	private void initialize()
	{
		JXStatusBar statusBar = createStatusBar();
		add(statusBar, BorderLayout.PAGE_END);
		_presenter.onInit();
	}

	@Override
	public void displayTxCount(int count)
	{
		String message = "" + count + " transactions.";

		_statusLabel.setText(message);
	}

	private JXStatusBar createStatusBar()
	{
		JXStatusBar sb = new JXStatusBar();

		_statusLabel = new JLabel();
		_statusLabel.setName("TX_COUNT");

		sb.add(_statusLabel);

		return sb;
	}

}
