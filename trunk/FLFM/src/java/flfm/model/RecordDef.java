package flfm.model;

import java.util.List;

/**
 * RecordDef
 * @author Kazuhiko Arase
 */
public class RecordDef {
	private String name;
	private String encoding;
	private List<FieldDef> fields;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public List<FieldDef> getFields() {
		return fields;
	}
	public void setFields(List<FieldDef> fields) {
		this.fields = fields;
	}
}