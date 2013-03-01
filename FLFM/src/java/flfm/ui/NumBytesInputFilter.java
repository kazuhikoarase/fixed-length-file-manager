package flfm.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * NumBytesInputFilter
 * @author Kazuhiko Arase
 */
public class NumBytesInputFilter extends DocumentFilter {
	
	private final String enc;
	private final int maxLength;

	public NumBytesInputFilter(final String enc, final int maxLength) {
		this.enc = enc;
		this.maxLength = maxLength;
	}
	
	private boolean isValid(final String text) {
		try {
			return text.getBytes(enc).length <= maxLength;
		} catch(Exception e) {
			return false;
		}
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		Document doc = fb.getDocument();
		String oldText = doc.getText(0, doc.getLength());
		StringBuilder buf = new StringBuilder(oldText);
		buf.insert(offset, string);
		if (isValid(buf.toString())) {
			super.insertString(fb, offset, string, attr);
		}
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		if (text == null) {
			return;
		}
		Document doc = fb.getDocument();
		String oldText = doc.getText(0, doc.getLength());
		StringBuilder buf = new StringBuilder(oldText);
		buf.replace(offset, offset + length, text);
		if (isValid(buf.toString())) {
			super.replace(fb, offset, length, text, attrs);
		}
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		Document doc = fb.getDocument();
		String oldText = doc.getText(0, doc.getLength());
		StringBuilder buf = new StringBuilder(oldText);
		buf.replace(offset, offset + length, "");
		if (isValid(buf.toString())) {
			super.remove(fb, offset, length);
		}
	}
}
