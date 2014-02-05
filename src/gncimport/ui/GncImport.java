package gncimport.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GncImport extends JPanel
{
	private static JFrame _mainFrame;

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

	public static void createAndShowGUI()
	{
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		JFrame frame = createMainFrame();
		frame.pack();
		frame.setVisible(true);
	}

	public static JFrame createMainFrame()
	{
		_mainFrame = new JFrame("GnuCash Transaction Import");

		_mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GncImport newContentPane = new GncImport();

		_mainFrame.setContentPane(newContentPane);

		return _mainFrame;
	}
}
