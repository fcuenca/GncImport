package gncimport;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import gncimport.models.LocalFileTxImportModel;
import gncimport.models.TxImportModel;
import gncimport.ui.GncImportMainWindow;

import javax.swing.JFrame;

public class GncImportApp
{
	public static String HomeDir = System.getProperty("user.home");
	
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
		LocalFileTxImportModel model = new LocalFileTxImportModel(DEFAULT_TARGET_ACCOUNT);
		
		model.setTransactionMatchingRules(new ConfigOptions(readConfigFile()));
		
		return createMainFrame(model);
	}

	private static Properties readConfigFile()
	{
		Properties p = new Properties();

		try
		{
			p.load(new FileInputStream(HomeDir + "/.gncimport"));
		}
		catch (FileNotFoundException e)
		{
			// It's OK, the config file is optional
		}
		catch (IOException e)
		{
			// TODO report exception properly and continue
		}
		
		return p;
	}

	private static JFrame createMainFrame(TxImportModel model)
	{		
		JFrame mainFrame = new JFrame("GnuCash Transaction Import");

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GncImportMainWindow newContentPane = new GncImportMainWindow(model);

		mainFrame.setContentPane(newContentPane);

		return mainFrame;
	}

}
