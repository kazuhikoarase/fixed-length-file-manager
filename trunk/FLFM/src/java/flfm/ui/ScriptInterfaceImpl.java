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
	private static final String TEXT = "TEXT";
	private static final String BLANK = "";
	private static final String DEFAULT_ENCODING = "ISO-8859-1";
	
	private ScriptEngine engine;
	private File selectedFile;
	private RandomAccessFile raf;
	private List<Record> recordList;
	private int nest;
	
	public ScriptInterfaceImpl(
		ScriptEngine engine,
		File selectedFile
	) throws Exception {
		this.engine = engine;
		this.selectedFile = selectedFile;
		this.raf = new RandomAccessFile(selectedFile, "r");
		this.recordList = new ArrayList<Record>();
		this.nest = 0;
		engine.put(CURRENT_DIR, getBaseDir() );
		// short hand
		engine.eval("var trace = function(msg) { si.trace(msg); };");
		engine.eval("var evalfile = function(src) { si.evalfile(src); };");
	}

	private File getBaseDir() throws Exception {
		File dir = selectedFile.getParentFile();
		while (dir != null) {
			File baseDir = new File(dir,
				Config.getInstance().getSystemFolderName() );
			if (baseDir.exists() ) {
				return baseDir;
			}
			// seek parent folder
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
	
	public Map<String, String> readRecord(
		String formatFile
	) throws Exception {
		return read(loadRecordDef(formatFile), false);
	}

	public Map<String, String> peekRecord(
		String formatFile
	) throws Exception {
		return read(loadRecordDef(formatFile), true);
	}

	public String readString(int lengthInBytes) throws Exception {
		return read(createRecordDef(lengthInBytes), false).get(TEXT);
	}

	public String peekString(int lengthInBytes) throws Exception {
		return read(createRecordDef(lengthInBytes), true).get(TEXT);
	}

	private RecordDef loadRecordDef(String formatFile) throws Exception {
		final File currentDir = (File)engine.get(CURRENT_DIR);
		final File file = new File(currentDir, formatFile);
		return new FormatLoader().load(file);
	}
	
	private RecordDef createRecordDef(int lengthInBytes) {

		FieldDef fd = new FieldDef();
		fd.setName(TEXT);
		fd.setComment(BLANK);
		fd.setDescription(BLANK);
		fd.setType(BLANK);
		fd.setSize(lengthInBytes);
		
		List<FieldDef> fields = new ArrayList<FieldDef>();
		fields.add(fd);

		RecordDef rd = new RecordDef();
		rd.setName(BLANK);
		rd.setEncoding(DEFAULT_ENCODING);
		rd.setFields(fields);
		rd.setVisible(false);
		return rd;
	}
	
	private Map<String, String> read(
		RecordDef rd,
		boolean peek
	) throws Exception {

		final long pos = raf.getFilePointer();
		
		final Map<String,String> dataMap = new HashMap<String, String>();

		dataMap.put(Record.RECORD_NAME,
			rd.getName().replaceAll("\\..*$", "") );

		for (FieldDef field : rd.getFields() ) {
			final byte[] b = new byte[field.getSize()];
			raf.readFully(b);
			final String data = new String(b, rd.getEncoding() );
			dataMap.put(field.getName(), data);
		}

		if (!peek) {
			final Record record = new Record();
			record.setRecordDef(rd);
			record.setDataMap(dataMap);
			record.setNest(nest);
			recordList.add(record);
		} else {
			raf.seek(pos);
		}
		
		return dataMap;
	}

	public void beginNest() {
		if (recordList.size() > 0) {
			Record lastRecord = recordList.get(recordList.size() - 1);
			nest = lastRecord.getNest() + 1;
			lastRecord.setLeaf(false);
		}
	}

	public void endNest() {
		if (nest > 0) {
			nest -= 1;
		}
	}
}
