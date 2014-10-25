package gncimport;

import gncimport.models.LocalFileTxImportModel;
import gncimport.models.TxImportModel;
import gncimport.ui.GncImportMainWindow;
import gncimport.ui.UIConfig;

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.apple.eawt.Application;
import com.apple.eawt.QuitStrategy;

/*
 * TO ALLOW THE USE OF THE APPLE CLASSES
 * 
 * - Open the Libraries tab of the Java Build Path project property window.
 * - Expand the JRE System Library entry. 
 * - Select "Access rules" and hit the Edit button. 
 * - Click the Add button in the resulting dialog. 
 * - For the new access rule, set the resolution to Accessible and the pattern to "com/apple/eawt/**"
 */


/*
 * TO GENERATE THE APP, create a Runnable Jar:
 * - File|Export
 * - Java/Runnable Jar
 * - Package required libs into JAR file 
 */

public class GncImportApp
{
	public static String HomeDir = System.getProperty("user.home");
	
	private static final String DEFAULT_TARGET_ACCOUNT = "Gastos Varios";

	public static void main(String[] args)
	{
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "GncImport");
		
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
		JFrame frame = createMainFrame();
		frame.pack();
		frame.setVisible(true);
	}

	public static JFrame createMainFrame()
	{
		LocalFileTxImportModel model = new LocalFileTxImportModel(DEFAULT_TARGET_ACCOUNT);
				
		final ConfigOptions config = new ConfigOptions(readPropertiesFromFile());
		
		model.setTransactionMatchingRules(config);
		
		JFrame frame = createMainFrame(model, config);
									
		frame.setJMenuBar(createMenuBar());
		setupAppShutdown(frame, config);
				
		return frame;
	}

	private static JMenuBar createMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Tools");
		JMenuItem menuItem = new JMenuItem("Create new Account Hierarchy");
		
		menuItem.setName("NEW_ACC_HIERARCHY");
		menu.add(menuItem);
		menuBar.add(menu);
		
		return menuBar;
	}

	private static void setupAppShutdown(JFrame frame, final ConfigOptions config)
	{
		// This will configure the Cmd-Q Mac Command to shutdown the app by closing all windows
		// (instead of just doing a System.exit()), which will trigger the windowClosing event below.
		
		Application a = Application.getApplication();
		a.setQuitStrategy(QuitStrategy.CLOSE_ALL_WINDOWS);
		
		// The End-to-End test, however, seems to be closing down the app by calling dispose() on the frame 
		// which triggers windowClosed instead. For some reason, the event is sent twice,
		// and that's why the listener removes itself from the frame (?!)

		frame.addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				saveConfigToFile(config);
				e.getWindow().removeWindowListener(this);
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				saveConfigToFile(config);
				System.exit(0);
			}
			
		});
	}

	private static JFrame createMainFrame(TxImportModel model, UIConfig config)
	{		
		JFrame mainFrame = new JFrame("GnuCash Transaction Import");

		GncImportMainWindow newContentPane = new GncImportMainWindow(model, config);
		
		mainFrame.setContentPane(newContentPane);

		return mainFrame;
	}

	public static void displayError(Component dlgOwner, Exception e)
	{
		String stackTrace = "";
	
		for (StackTraceElement se : e.getStackTrace())
		{
			stackTrace += se.toString() + "\n";
		}
	
		JXErrorPane.showDialog(dlgOwner,
				new ErrorInfo("Critical Error",
						e.getMessage(),
						stackTrace,
						null,
						e, Level.SEVERE, null));
	}

	private static void saveConfigToFile(ConfigOptions config)
	{		
		Properties p = config.getProperties();
		
		try
		{
			p.store(new FileOutputStream(configFileName()), null);
		}
		catch (Exception e)
		{
			displayError(null, e);
		}
	}

	private static Properties readPropertiesFromFile()
	{
		Properties p = new Properties();

		try
		{
			p.load(new FileInputStream(configFileName()));
		}
		catch (FileNotFoundException e)
		{
			// It's OK, the config file is optional
		}
		catch (IOException e)
		{
			GncImportApp.displayError(null, e);
		}
		
		return p;
	}

	private static String configFileName()
	{
		return HomeDir + "/.gncimport";
	}

}
