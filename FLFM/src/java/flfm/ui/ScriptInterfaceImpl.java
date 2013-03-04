package flfm.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;

import flfm.core.Config;
import flfm.io.FormatLoader;
import flfm.model.FieldDef;
import flfm.model.Record;
import flfm.model.RecordDef;
import flfm.scripting.IScriptInterface;

/**
 * ScriptInterfaceImpl
 * @author Kazuhiko Arase
 */
public class ScriptInterfaceImpl implements IScriptInterface {

	private static final String CURRENT_DIR = "__current_dir__";
	
	private File baseDir;
	private ScriptEngine engine;
	private File selectedFile;
	private RandomAccessFile raf;
	private List<Record> recordList;
	
	public ScriptInterfaceImpl(File baseDir, ScriptEngine engine, File selectedFile) 
	throws Exception {
		this.baseDir = baseDir;
		this.engine = engine;
		this.selectedFile = selectedFile;
		this.raf = new RandomAccessFile(selectedFile, "r");
		this.recordList = new ArrayList<Record>();
		engine.put(CURRENT_DIR, baseDir);
	}

	public Object evalfile(String path) throws Exception {
		File currentDir = (File)engine.get(CURRENT_DIR);
		File srcFile = new File(currentDir, path);
		Reader in = new BufferedReader(new InputStreamReader(
			new FileInputStream(srcFile), 
			Config.getInstance().getResourceEncoding() ) );
		try {
			engine.put(CURRENT_DIR, srcFile.getParentFile() );
			engine.put(ScriptEngine.FILENAME, srcFile.getAbsolutePath() );
			Object retval = engine.eval(in);
			engine.put(CURRENT_DIR, currentDir);
			return retval;
		} finally {
			in.close();
		}
	}

	public void trace(Object msg) throws Exception {
		System.out.println(msg);
	}
	
	public File getDataFile() throws Exception {
		return selectedFile;
	}
	
	public void dispose() throws Exception {
		raf.close();
	}

	public long getLength() throws Exception {
		return raf.length();
	}

	public long getPosition() throws Exception {
		return raf.getFilePointer();
	}
	
	public void setPosition(long position) throws Exception {
		raf.seek(position);
	}
	
	public boolean isEOF() throws Exception {
		return !(getPosition() < getLength() );
	}

	public List<Record> getRecordList() {
		return recordList;
	}
	
	public Map<String, String> readRecord(String formatFile) throws Exception {
		return readImpl(formatFile, false);
	}

	public Map<String, String> peekRecord(String formatFile) throws Exception {
		return readImpl(formatFile, true);
	}

	private Map<String, String> readImpl(String formatFile, boolean peek)
	throws Exception {

		long pos = raf.getFilePointer();

		File assetsFolder = new File(selectedFile.getParent(), 
				Config.getInstance().getSystemFolderName() );
		File file = new File(assetsFolder, formatFile);
		RecordDef rd = new FormatLoader().load(file);

		Map<String,String> map = new HashMap<String, String>();
		List<String> dataList = new ArrayList<String>();
		for (FieldDef field : rd.getFields() ) {
			byte[] b = new byte[field.getSize()];
			raf.readFully(b);
			String data = new String(b, rd.getEncoding() );
			map.put(field.getName(), data);
			dataList.add(data);
		}

		if (!peek) {
			Record record = new Record();
			record.setRecordDef(rd);
			record.setDataList(dataList);
			recordList.add(record);
		} else {
			raf.seek(pos);
		}
		
		return map;
	}
}
