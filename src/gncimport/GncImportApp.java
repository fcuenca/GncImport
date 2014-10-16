package gncimport;

import gncimport.models.LocalFileTxImportModel;
import gncimport.models.TxImportModel;
import gncimport.ui.GncImportMainWindow;

import javax.swing.JFrame;

public class GncImportApp
{
	private static final String DEFAULT_TARGET_ACCOUNT = "Gastos Varios";

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
		return createMainFrame(new LocalFileTxImportModel(DEFAULT_TARGET_ACCOUNT));
	}

	public static JFrame createMainFrame(TxImportModel model)
	{
		JFrame mainFrame = new JFrame("GnuCash Transaction Import");

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GncImportMainWindow newContentPane = new GncImportMainWindow(model);

		mainFrame.setContentPane(newContentPane);

		return mainFrame;
	}

}
