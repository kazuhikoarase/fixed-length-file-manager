package flfm.ui;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import flfm.model.FieldDef;
import flfm.model.Record;

/**
 * RecordEditor
 * @author Kazuhiko Arase
 */
@SuppressWarnings("serial")
public class RecordEditor extends JComponent {

	private Record model;

	private DocumentListener documentListener;

	private DocumentListener documentListenerDlg = new DocumentListener() {
		public void removeUpdate(DocumentEvent e) {
			updateModel();
			documentListener.removeUpdate(e);
		}
		public void insertUpdate(DocumentEvent e) {
			updateModel();
			documentListener.insertUpdate(e);
		}
		public void changedUpdate(DocumentEvent e) {
			updateModel();
			documentListener.changedUpdate(e);
		}
	};
	
	private List<JTextField> fieldEditors;

	public RecordEditor() {
	}

	public DocumentListener getDocumentListener() {
		return documentListener;
	}

	public void setDocumentListener(DocumentListener documentListener) {
		this.documentListener = documentListener;
	}

	public void setModel(Record model) {

		this.model = model;
		
		removeAll();
		fieldEditors = new ArrayList<JTextField>();
		
		setLayout(new RecordEditorLayout() );

		// ヘッダ
		add(new JLabel("No.") );
		add(new JLabel("Name") );
		add(new JLabel("Comment") );
		add(new JLabel("Type") );
		add(new JLabel("Size(b)") );
		add(new JLabel("Content") );

		int totalSize = 0;
		
		for (int i = 0; i < model.getRecordDef().getFields().size(); i += 1) {

			FieldDef fd = model.getRecordDef().getFields().get(i);
			add(createLabel(String.valueOf(i + 1), SwingConstants.RIGHT) );
			add(createLabel(fd.getName() ) );
			add(createLabel(fd.getComment() ) );
			add(createLabel(fd.getType() ) );
			add(createLabel(String.valueOf(fd.getSize() ),
					SwingConstants.RIGHT) );

			JTextField tf = new DataEditor(model, fd);
			if (fd.getDescription().length() > 0) {
				tf.setToolTipText(fd.getDescription() );
			}
			tf.setText(model.getDataList().get(i) );
			tf.select(0, 0);
			tf.getDocument().addDocumentListener(documentListenerDlg);

			add(tf);
			fieldEditors.add(tf);
			
			totalSize += fd.getSize();
		}

		// フッタ
		add(new JLabel("") );
		add(new JLabel("") );
		add(new JLabel("") );
		add(new JLabel("") );
		add(createLabel(String.valueOf(totalSize),
				SwingConstants.RIGHT) );
		add(new JLabel("") );
	}
	
	public Record getModel() {
		return model;
	}

	private void updateModel() {
		for (int i = 0; i < model.getRecordDef().getFields().size(); i += 1) {
			JTextField tf = fieldEditors.get(i);
			model.getDataList().set(i, tf.getText() );
		}		
	}

	private JLabel createLabel(String text, int align) {
		JLabel label = new JLabel(text, align);
		label.setFont(new Font("sansserif", Font.PLAIN, 12) );
		return label;
	}
	
	private JLabel createLabel(String text) {
		return createLabel(text, SwingConstants.LEFT);
	}
}