package flfm.ui;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import flfm.model.Record;

/**
 * FileOpenDialog
 * @author Kazuhiko Arase
 */
@SuppressWarnings("serial")
public class FileOpenDialog extends JDialog {

	private static final String CANCELED = "FileOpenDialog$canceled$";
	
    private JProgressBar progress;
    private File file;
    private boolean alive = false;
    private boolean success = true;
    private List<Record> recordList = null;
    private transient Thread thread;

    public FileOpenDialog(MainFrame mainFrame, File file) {

        super(mainFrame, true);

        this.file = file;
        
        progress = new JProgressBar(0, 100);
        progress.setBorder(new EmptyBorder(4, 4, 4, 4) );
        progress.setString("");
        progress.setStringPainted(true);

        Action exitAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                alive = false;    
            }
        };
        exitAction.putValue(Action.NAME, "Cancel");
                
        JPanel p = new JPanel();
        p.add(new JButton(exitAction) );
        getContentPane().add("Center", progress);
        getContentPane().add("South", p);

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
    			thread = new Thread(new Runnable() {
                    public void run() {
                        task();
                    }
                });
                thread.start();
            }
        });
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public boolean isSuccess() {
		return success;
	}

	public List<Record> getRecordList() {
		return recordList;
	}

	private void task() {

        alive = true;

        try {

    		ScriptEngineManager manager = new ScriptEngineManager();
    		ScriptEngine engine = manager.getEngineByName("ECMAScript");

    		ScriptInterfaceImpl si = new ScriptInterfaceImpl(engine, file,
				new ScriptInterfaceImpl.IProgressHandler() {
	    			public void updateProgress(double pos, double len) {
	    				int percent = (int)(pos / len * 100);
						progress.setValue(percent);
						progress.setString(percent + "%");
						if (!alive) {
							throw new RuntimeException(CANCELED);
						}
	    			}
				}
    		);
    		
    		try {

    			engine.put("si", si);
    			si.evalfile("main.js");

        		si.updateProgress();
    			Thread.sleep(50);

    		} finally {
    			si.dispose();
    		}

    		recordList = si.getRecordList();

        } catch(Exception e) {
        	handleException(e);
        } finally {
            dispose();
        }
    }

	private void handleException(Exception e) {

		success = false;
		if (e.getMessage().indexOf(CANCELED) != -1) {
			return;
		}
		
		e.printStackTrace();
		JOptionPane.showMessageDialog(
			this, e.getMessage(), "Error",
			JOptionPane.ERROR_MESSAGE);
	}
}
