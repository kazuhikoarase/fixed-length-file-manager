package flfm.model;

import java.util.Map;

/**
 * Record
 * @author Kazuhiko Arase
 */
public class Record {
	
	public static final String RECORD_NAME = "__RECORD_NAME__";
	
	private String name = "";
	private int nest = 0;
	private boolean leaf = true;
	private RecordDef recordDef = null;
	private Map<String, String> dataMap = null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNest() {
		return nest;
	}
	public void setNest(int nest) {
		this.nest = nest;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public RecordDef getRecordDef() {
		return recordDef;
	}
	public void setRecordDef(RecordDef recordDef) {
		this.recordDef = recordDef;
	}
	public Map<String, String> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}
	public String toString() {
		if (name == null) {
			return super.toString();
		}
		return name;
	}
}