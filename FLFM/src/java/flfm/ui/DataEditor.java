package flfm.ui;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import flfm.model.FieldDef;
import flfm.model.Record;

/**
 * DataEditor
 * @author Kazuhiko Arase
 */
@SuppressWarnings("serial")
public class DataEditor extends JTextField {
	public DataEditor(Record record, FieldDef fd) {
		setColumns(fd.getSize() );
		setFont(new Font("monospaced", Font.PLAIN, 12) );
		( (PlainDocument)getDocument() ).setDocumentFilter(
			new NumBytesInputFilter(record.getRecordDef().getEncoding(),
					fd.getSize() ) );
		addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				JTextField tf = (JTextField)e.getSource();
				tf.select(0, 0);
			}
			public void focusGained(FocusEvent e) {
			}
		} );
	}
}
