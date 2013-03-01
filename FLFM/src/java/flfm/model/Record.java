package flfm.model;

import java.util.List;

/**
 * Record
 * @author Kazuhiko Arase
 */
public class Record {
	private RecordDef recordDef;
	private List<String> dataList;
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
}