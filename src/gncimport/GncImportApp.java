package gncimport;

import gncimport.boundaries.TxModel;
import gncimport.models.FakeTxModel;
import gncimport.ui.GncImportMainWindow;

import javax.swing.JFrame;

public class GncImportApp
{
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
		JFrame mainFrame = new JFrame("GnuCash Transaction Import");

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GncImportMainWindow newContentPane = new GncImportMainWindow(model);

		mainFrame.setContentPane(newContentPane);

		return mainFrame;
	}

}