package flfm.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import flfm.core.Config;
import flfm.model.Record;

/**
 * MainFrame
 * @author Kazuhiko Arase
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private JDesktopPane desktop;

	private JFileChooser chooser;
	
	public MainFrame() throws Exception {

		setTitle("FLFM");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		Action openAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					open();
				} catch(Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		};
		openAction.putValue(Action.NAME, "Open");
		fileMenu.add(openAction);

		Action exitAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		};
		exitAction.putValue(Action.NAME, "Exit");
		fileMenu.add(exitAction);

		setJMenuBar(menuBar);

		desktop = new JDesktopPane();
		getContentPane().add(desktop);

		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("sample") );
		chooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "Fixed Length File";
			}
			
			@Override
			public boolean accept(File file) {
				if (file.getName().startsWith(".") ) {
					return false;
				} else if (file.getName().equals(
						Config.getInstance().getSystemFolderName() ) ) {
					return false;
				}
				return true;
			}
		} );
	}

	private void open() throws Exception {

		int ret = chooser.showOpenDialog(this);
		if (ret != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File selectedFile = chooser.getSelectedFile();
		File mainJs = new File(new File(selectedFile.getParentFile(),
				Config.getInstance().getSystemFolderName() ), "main.js");
		
		ScriptInterfaceImpl si = new ScriptInterfaceImpl(selectedFile);
		
		try {

	    	ScriptEngineManager manager = new ScriptEngineManager();
	        ScriptEngine engine = manager.getEngineByName("ECMAScript");

			Reader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(mainJs), 
					Config.getInstance().getResourceEncoding() ) );
			try {
				engine.put(ScriptEngine.FILENAME, mainJs.getAbsolutePath() );
				engine.put("si", si);
				engine.eval(in);
			} finally {
				in.close();
			}

		} finally {
			si.dispose();
		}

		// create ui
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		for (int r = 0; r < si.getRecordList().size(); r += 1) {

			Record record = si.getRecordList().get(r);
			
			RecordEditor editor = new RecordEditor();
			editor.setModel(record.getRecordDef() );
			
			for (int i = 0; i < record.getDataList().size(); i += 1) {
				JTextField tf = editor.getFieldEditorAt(i);
				tf.setText(record.getDataList().get(i) );
				tf.select(0, 0);
			}

			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEFT) );
			panel.add(editor);
			tabbedPane.add(
				"[" + (r + 1) + "] " + 
					record.getRecordDef().getName().replaceAll("\\..*$", ""),
				new JScrollPane(panel) );
		}

		JInternalFrame iframe = new JInternalFrame(selectedFile.getName() );
		iframe.setResizable(true);
		iframe.setMaximizable(true);
		iframe.setClosable(true);
		//TODO
		//iframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		iframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		iframe.getContentPane().add(tabbedPane);
		iframe.setSize(400, 300);
		iframe.setVisible(true);
		
		desktop.add(iframe);

		iframe.toFront();
	}
	
	private void exit() {
		System.exit(0);
	}
}