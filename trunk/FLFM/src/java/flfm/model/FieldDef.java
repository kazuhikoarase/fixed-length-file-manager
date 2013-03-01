package flfm.model;

/**
 * FieldDef
 * @author Kazuhiko Arase
 */
public class FieldDef {
	private String name;
	private String comment;
	private String type;
	private int size;
	public FieldDef() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}