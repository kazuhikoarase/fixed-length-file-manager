package flfm.ui;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private File selectedFile;
	private RandomAccessFile raf;
	private List<Record> recordList;
	
	public ScriptInterfaceImpl(File selectedFile) 
	throws Exception {
		this.selectedFile = selectedFile;
		this.raf = new RandomAccessFile(selectedFile, "r");
		this.recordList = new ArrayList<Record>();
	}
	
	public void dispose() throws Exception {
		raf.close();
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
