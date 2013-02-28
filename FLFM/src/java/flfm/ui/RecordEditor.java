package flfm.ui;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.PlainDocument;

import flfm.model.FieldDef;
import flfm.model.RecordDef;

@SuppressWarnings("serial")
public class RecordEditor extends JComponent {

	private static final String ENCODING = "MS932";
	
	private RecordDef model;
	private List<JTextField> fieldEditors;
	
	public RecordEditor() {
	}

	public void setModel(RecordDef model) {

		this.model = model;
		
		removeAll();
		fieldEditors = new ArrayList<JTextField>();
		
		setLayout(new RecordEditorLayout() );

		// ƒwƒbƒ_
		add(new JLabel("No.") );
		add(new JLabel("Name") );
		add(new JLabel("Comment") );
		add(new JLabel("Type") );
		add(new JLabel("Size(b)") );
		add(new JLabel("Content") );

		for (int i = 0; i < model.getFields().size(); i += 1) {
			FieldDef fd = model.getFields().get(i);
			add(createLabel(String.valueOf(i + 1), SwingConstants.RIGHT) );
			add(createLabel(fd.getName() ) );
			add(createLabel(fd.getComment() ) );
			add(createLabel(fd.getType() ) );
			add(createLabel(String.valueOf(fd.getSize() ),
					SwingConstants.RIGHT) );

			JTextField fe = createFieldEditor(fd);
			add(fe);
			fieldEditors.add(fe);
		}
	}
	
	public RecordDef getModel() {
		return model;
	}

	public JTextField getFieldEditorAt(int index) {
		return fieldEditors.get(index);
	}
	
	private JTextField createFieldEditor(FieldDef fd) {
		JTextField tf = new JTextField(fd.getSize() );
		tf.setFont(new Font("monospaced", Font.PLAIN, 12) );
		((PlainDocument)tf.getDocument() ).setDocumentFilter(
			new NumBytesInputFilter(ENCODING, fd.getSize() ) );
		tf.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				JTextField tf = (JTextField)e.getSource();
				tf.select(0, 0);
			}
			public void focusGained(FocusEvent e) {
			}
		});
		return tf;
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