package flfm.ui;

import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import flfm.core.Config;
import flfm.io.RecordIO;
import flfm.model.FieldDef;
import flfm.model.Record;
import flfm.model.RecordDef;
import flfm.model.SimpleTreeNode;

/**
 * MainFrame
 * @author Kazuhiko Arase
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JSplitPane splitPane = null;

	private List<Record> recordList = null;
	
	private JTree tree = null;
	
	private JFileChooser chooser = null;
	
	private File selectedFile = null;

	private Record selectedRecord = null;
	
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

		Action reopenAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					reopen();
				} catch(Exception ex) {
					handleException(ex);
				}
			}
		};
		reopenAction.putValue(Action.NAME, "Reopen");
		reopenAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
		reopenAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0) );
		fileMenu.add(reopenAction);

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

		
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic('E');
		menuBar.add(editMenu);

		Action copyRecordAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					copyRecord();
				} catch(Exception ex) {
					handleException(ex);
				}
			}
		};
		copyRecordAction.putValue(Action.NAME, "Copy Record");
		copyRecordAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
		editMenu.add(copyRecordAction);
		
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

		tree = new JTree();
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = tree.getSelectionPath();
				if (path == null) {
					return;
				}
				SimpleTreeNode node = (SimpleTreeNode)path.getLastPathComponent();
				Record record = (Record)node.getUserObject();
				if (record == null) {
					return;
				}
				selectRecord(record);
			}
		});
		tree.setModel(new DefaultTreeModel(new SimpleTreeNode() ) );

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(200);
		splitPane.setDividerSize(4);
		splitPane.setLeftComponent(new JScrollPane(tree) );
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
	
	private void reopen() throws Exception {
		if (selectedFile != null) {
			open(selectedFile);
		}
	}
	
	private void open(File selectedFile) throws Exception {
/*
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("ECMAScript");

		ScriptInterfaceImpl si = new ScriptInterfaceImpl(
				engine, selectedFile);
		try {
			engine.put("si", si);
			si.evalfile("main.js");
		} finally {
			si.dispose();
		}
*/
		Rectangle r = getBounds();
		
		FileOpenDialog dialog = new FileOpenDialog(this, selectedFile);
		dialog.pack();
		dialog.setLocation(r.x + (r.width - dialog.getWidth() ) / 2, r.x + (r.height - dialog.getHeight() ) / 2);
		dialog.setVisible(true);
		if (!dialog.isSuccess() ) {
			return;
		}
		
		this.selectedFile = selectedFile;
		updateTitle();
		setRecordList(dialog.getRecordList() );
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
	
	private void setRecordList(List<Record> recordList) {
		
		this.recordList = recordList;
		
		SimpleTreeNode root = new SimpleTreeNode();
		root.setLeaf(false);

		SimpleTreeNode currNode = root;
		SimpleTreeNode lastNode = null;

		for (int r = 0; r < recordList.size(); r += 1) {

			Record record = recordList.get(r);

			if (!record.getRecordDef().isVisible() ) {
				continue;
			}

			if (lastNode != null) {
				Record lastRecord = (Record)lastNode.getUserObject();
				if (record.getNest() > lastRecord.getNest() ) {
					// nest down
					currNode = lastNode;
				} else if (record.getNest() < lastRecord.getNest() ) {
					// nest up
					int n = lastRecord.getNest() - record.getNest();
					while (n > 0) {
						currNode = (SimpleTreeNode)currNode.getParent();
						n -= 1;
					}
				}
			}

			String recordName = record.getDataMap().get(Record.RECORD_NAME);
			record.setName(recordName);

			SimpleTreeNode node = new SimpleTreeNode(record);
			node.setLeaf(record.isLeaf() );
			currNode.addChild(node);
			lastNode = node;
		}

		tree.setModel(new DefaultTreeModel(root) );
		if (tree.getRowCount() > 0) {
			tree.setSelectionRow(0);
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
		
		selectedRecord = record;
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
			for (Record record : recordList) {
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

	private void copyRecord() throws Exception {
		if (selectedRecord == null) {
			return;
		}
		RecordDef rd = selectedRecord.getRecordDef();
		
		StringBuilder buf = new StringBuilder();

		buf.append("No.\tName\tComment\tType\tSize\tContent\r\n");

		int lineNo = 0;
		for (FieldDef fd : rd.getFields() ) {
			lineNo += 1;
			buf.append(lineNo);
			buf.append('\t');
			buf.append(fd.getName() );
			buf.append('\t');
			buf.append(fd.getComment() );
			buf.append('\t');
			buf.append(fd.getType() );
			buf.append('\t');
			buf.append(fd.getSize() );
			buf.append('\t');
			buf.append(selectedRecord.getDataMap().get(fd.getName() ) );
			buf.append('\r');
			buf.append('\n');
		}

		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection ss = new StringSelection(buf.toString() );
		cb.setContents(ss, ss);
	}

	private void handleException(Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(
			this, e.getMessage(), "Error",
			JOptionPane.ERROR_MESSAGE);
	}
}