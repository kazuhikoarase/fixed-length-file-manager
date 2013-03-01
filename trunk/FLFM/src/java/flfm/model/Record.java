package flfm.model;

import java.util.List;

/**
 * Record
 * @author Kazuhiko Arase
 */
public class Record {
	private String name;
	private RecordDef recordDef;
	private List<String> dataList;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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