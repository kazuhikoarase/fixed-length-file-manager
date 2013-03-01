package flfm.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import flfm.core.Config;
import flfm.io.RecordIO;
import flfm.model.Record;

/**
 * MainFrame
 * @author Kazuhiko Arase
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JSplitPane splitPane = null;

	private JList list = null;
	
	private JFileChooser chooser = null;
	
	private File selectedFile = null;

	private boolean modified = false;
	
	private DocumentListener documentListener = new DocumentListener() {
		public void removeUpdate(DocumentEvent e) {
			setModified();
		}
		public void insertUpdate(DocumentEvent e) {
			setModified();
		}
		public void changedUpdate(DocumentEvent e) {
			setModified();
		}
	};
	
	public MainFrame() throws Exception {

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					exit();
				} catch(Exception ex) {
					handleException(ex);
				}
			}
		});
		
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);

		Action openAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					open();
				} catch(Exception ex) {
					handleException(ex);
				}
			}
		};
		openAction.putValue(Action.NAME, "Open");
		openAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		fileMenu.add(openAction);

		Action saveAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch(Exception ex) {
					handleException(ex);
				}
			}
		};
		saveAction.putValue(Action.NAME, "Save");
		saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveAction.putValue(Action.ACCELERATOR_KEY,
			KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK) );
		fileMenu.add(saveAction);

		Action saveAsAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveAs();
				} catch(Exception ex) {
					handleException(ex);
				}
			}
		};
		saveAsAction.putValue(Action.NAME, "Save As");
		saveAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		fileMenu.add(saveAsAction);

		Action exitAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					exit();
				} catch(Exception ex) {
					handleException(ex);
				}
			}
		};
		exitAction.putValue(Action.NAME, "Exit");
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		fileMenu.add(exitAction);

		setJMenuBar(menuBar);

		String folder = Config.getInstance().getCurrentFolder();
		
		chooser = new JFileChooser();
		if (folder != null) {
			chooser.setCurrentDirectory(new File(folder) );
		}
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

		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Record record = (Record)list.getSelectedValue();
				if (record != null) {
					selectRecord(record);
				}
			}
		});
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(200);
		splitPane.setDividerSize(4);
		splitPane.setLeftComponent(new JScrollPane(list) );
		splitPane.setRightComponent(new JPanel() );
		
		getContentPane().add(splitPane);

		updateTitle();
	}

	private void open() throws Exception {

		int ret = chooser.showOpenDialog(this);
		if (ret != JFileChooser.APPROVE_OPTION) {
			return;
		}
		open(chooser.getSelectedFile() );
	}

	private void open(File selectedFile) throws Exception {
	
		File mainJs = new File(new File(selectedFile.getParentFile(),
				Config.getInstance().getSystemFolderName() ), "main.js");
		
		ScriptInterfaceImpl si = new ScriptInterfaceImpl(selectedFile);
		
		try {

	    	ScriptEngineManager manager = new ScriptEngineManager();
	        ScriptEngine engine = manager.getEngineByName("ECMAScript");

	        engine.eval("var trace = function(msg) {" +
        		"java.lang.System.out.println('' + msg);};");
	        
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

		this.selectedFile = selectedFile;
		updateTitle();
		updateRecordList(si.getRecordList() );
	}

	private void updateTitle() {
		StringBuilder buf = new StringBuilder();
		buf.append("FLFM");
		if (selectedFile != null) {
			buf.append(" - ");
			buf.append(selectedFile.getAbsolutePath() );
			if (modified) {
				buf.append(" *");
			}
		}
		setTitle(buf.toString() );
	}

	private void setModified() {
		modified = true;
		updateTitle();
	}
	
	private void updateRecordList(List<Record> recordList) {
		
		DefaultListModel listModel = new DefaultListModel();
		for (int r = 0; r < recordList.size(); r += 1) {
			Record record = recordList.get(r);
			record.setName("[" + (r + 1) + "] " + 
				record.getRecordDef().getName().replaceAll("\\..*$", "") );
			listModel.addElement(record);
		}

		list.setModel(listModel);
		if (recordList.size() > 0) {
			list.setSelectedIndex(0);
		}
	}
	
	private void selectRecord(Record record) {

		RecordEditor editor = new RecordEditor();
		editor.setModel(record);
		editor.setDocumentListener(documentListener);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT) );
		panel.add(editor);
		
		int lastLoc = splitPane.getDividerLocation();
		splitPane.setRightComponent(new JScrollPane(panel) );
		splitPane.setDividerLocation(lastLoc);
	}
	
	public void save() throws Exception {
		if (selectedFile == null) {
			return;
		}
		saveImpl(selectedFile);
	}
	
	private void saveAs() throws Exception {

		int ret = chooser.showSaveDialog(this);
		if (ret != JFileChooser.APPROVE_OPTION) {
			return;
		}
		saveImpl(chooser.getSelectedFile() );
	}

	private void saveImpl(File file) throws Exception {

		OutputStream out = new BufferedOutputStream(
				new FileOutputStream(file) );
		try {
			for (int i = 0; i < list.getModel().getSize(); i += 1) {
				Record record = (Record)list.getModel().getElementAt(i);
				RecordIO.writeRecord(out, record);
			}
		} finally {
			out.close();
		}
		
		selectedFile = file;
		modified = false;
		updateTitle();
	}
	
	private void exit() throws Exception {
		
		if (modified) {
			int ret = JOptionPane.showConfirmDialog(this, 
				"'" + selectedFile.getName() +
				"' has been modified. Save changes?", "",
				JOptionPane.YES_NO_CANCEL_OPTION);
			if (ret == JOptionPane.CANCEL_OPTION) {
				return;
			} else if (ret == JOptionPane.YES_OPTION) {
				save();
			}
		}
		
		// store settings
		Config.getInstance().setCurrentFolder(
			chooser.getCurrentDirectory().getAbsolutePath() );
		Config.getInstance().store();

		System.exit(0);
	}
	
	private void handleException(Exception e) {
		JOptionPane.showMessageDialog(
			this, e.getMessage(), "Error",
			JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	}
}