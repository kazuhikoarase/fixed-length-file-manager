package flfm.model;

import java.util.List;

public class RecordDef {
	private String name;
	private List<FieldDef> fields;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<FieldDef> getFields() {
		return fields;
	}
	public void setFields(List<FieldDef> fields) {
		this.fields = fields;
	}
}