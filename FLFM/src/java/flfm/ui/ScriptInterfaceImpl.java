package flfm.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
	
	private ScriptEngine engine;
	private File selectedFile;
	private RandomAccessFile raf;
	private List<Record> recordList;
	
	public ScriptInterfaceImpl(ScriptEngine engine, File selectedFile) 
	throws Exception {
		this.engine = engine;
		this.selectedFile = selectedFile;
		this.raf = new RandomAccessFile(selectedFile, "r");
		this.recordList = new ArrayList<Record>();
		engine.put(CURRENT_DIR, getBaseDir() );
	}

	private File getBaseDir() throws Exception {
		File dir = selectedFile.getParentFile();
		while (dir != null) {
			File baseDir = new File(dir,
				Config.getInstance().getSystemFolderName() );
			if (baseDir.exists() ) {
				return baseDir;
			}
			// 親フォルダを探す
			dir = dir.getParentFile();
		}
		throw new IOException("base dir not found.");
	}
	
	public Object evalfile(String path) throws Exception {
		final File currentDir = (File)engine.get(CURRENT_DIR);
		final File srcFile = new File(currentDir, path);
		final Reader in = new BufferedReader(new InputStreamReader(
			new FileInputStream(srcFile), 
			Config.getInstance().getResourceEncoding() ) );
		try {
			engine.put(CURRENT_DIR, srcFile.getParentFile() );
			engine.put(ScriptEngine.FILENAME, srcFile.getAbsolutePath() );
			final Object retval = engine.eval(in);
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

	public String peekString(int lengthInBytes) throws Exception {
		final long pos = raf.getFilePointer();
		String s = readString(lengthInBytes);
		raf.seek(pos);
		return s;
	}

	public String readString(int lengthInBytes) throws Exception {
		final byte[] b = new byte[lengthInBytes];
		raf.readFully(b);
		return new String(b, "ISO-8859-1");
	}

	private Map<String, String> readImpl(String formatFile, boolean peek)
	throws Exception {

		final File currentDir = (File)engine.get(CURRENT_DIR);
		final long pos = raf.getFilePointer();

		final File file = new File(currentDir, formatFile);
		final RecordDef rd = new FormatLoader().load(file);

		final Map<String,String> map = new HashMap<String, String>();
		final List<String> dataList = new ArrayList<String>();
		for (FieldDef field : rd.getFields() ) {
			final byte[] b = new byte[field.getSize()];
			raf.readFully(b);
			final String data = new String(b, rd.getEncoding() );
			map.put(field.getName(), data);
			dataList.add(data);
		}

		if (!peek) {
			final Record record = new Record();
			record.setRecordDef(rd);
			record.setDataList(dataList);
			recordList.add(record);
		} else {
			raf.seek(pos);
		}
		
		return map;
	}
}
