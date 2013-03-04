package flfm.io;

import java.util.ArrayList;
import java.util.List;

public class CSVReader {

	private static final char CR = '\r';
	private static final char LF = '\n';
	private static final char QUOT = '"';
	private static final char TAB = '\t';

	private char delm;
	private Reader reader;
	private boolean quoted;
	private StringBuilder buf;
	private boolean newLine;
	private IHandler handler;
	
	public CSVReader() {
		this(TAB);
	}
	
	public CSVReader(char delm) {
		this.delm = delm;
	}

	public void parse(String s, IHandler handler){

		this.reader = new Reader(s);
		this.handler = handler;
		this.quoted = false;
		this.buf = new StringBuilder();
		this.newLine = true;

		while (true) {
			int c = reader.read();
			if (c == -1) {
				if (quoted) {
					throw new RuntimeException("unexpected eof");
				} else {
					break;
				}
			} else if (quoted) {
				readQuoted(c);
			} else {
				read(c);
			}
		}
		if (!newLine) {
			item(false);
		}
	}

	private void item(boolean eol) {
		handler.item(buf.toString() );
		buf = new StringBuilder();
		if (eol) {
			handler.eol();
			newLine = true;
		}
	}

	private void read(int c) {

		newLine = false;

		if (c == CR || c == LF) {
			
			item(true);
	
			if (c == CR) {
				// read following LF
				c = reader.read();
				if (c == -1) {
					// skip
				} else if (c == LF) {
					// skip
				} else {
					read( (char)c);
				}
			}
			
		} else if (c == QUOT) {
			quoted = true;
		} else if (c == delm) {
			item(false);
		} else {
			buf.append( (char)c);
		}
	}

	private void readQuoted(int c) {
		if (c == QUOT) {
			c = reader.read();
			if (c == QUOT) {
				buf.append(QUOT);
			} else {
				quoted = false;
				if (c != -1) {
					read(c);
				}
			}
		} else {
			buf.append( (char)c);
		}
	}

	public List<List<String>> toDataArray(String s) {
		return new ToDataArray_().toDataArray(s);
	}

	public interface IHandler {
		void item(String buf);
		void eol();
	}

	private class ToDataArray_ {
		private List<String> row;
		private List<List<String>> rows;
		public List<List<String>> toDataArray(String s) {
			row = null;
			rows = new ArrayList<List<String>>();
			parse(s, new IHandler() {
				public void eol() {
					row = null;
				}
				public void item(String item) {
					if (row == null) {
						rows.add(row = new ArrayList<String>() );
					}
					row.add(item);
				}
			} );
			return rows;
		}
	}

	private static class Reader {
		private String s;
		private int index;
		public Reader(String s) {
			this.s = s;
			this.index = 0;
		}
		public int read() {
			if (index < s.length() ) {
				char c = s.charAt(index);
				index += 1;
				return c;
			} else {
				return -1;
			}
		}
	}
}