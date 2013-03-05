package flfm.model;

import java.util.List;

/**
 * RecordDef
 * @author Kazuhiko Arase
 */
public class RecordDef {
	private String name;
	private String encoding;
	private boolean visible;
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
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public List<FieldDef> getFields() {
		return fields;
	}
	public void setFields(List<FieldDef> fields) {
		this.fields = fields;
	}
}