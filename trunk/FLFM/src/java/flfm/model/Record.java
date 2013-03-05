package flfm.model;

import java.util.List;

/**
 * Record
 * @author Kazuhiko Arase
 */
public class Record {
	private String name = "";
	private int nest = 0;
	private boolean leaf = true;
	private RecordDef recordDef = null;
	private List<String> dataList = null;
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
	public List<String> getDataList() {
		return dataList;
	}
	public void setDataList(List<String> dataList) {
		this.dataList = dataList;
	}
	public String toString() {
		if (name == null) {
			return super.toString();
		}
		return name;
	}
}