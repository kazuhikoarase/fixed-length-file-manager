package flfm.io;

import java.io.IOException;
import java.io.OutputStream;

import flfm.model.FieldDef;
import flfm.model.Record;

/**
 * FormatLoader
 * @author Kazuhiko Arase
 */
public class RecordIO {

	private RecordIO() {
	}

	public static void writeRecord(OutputStream out, Record record)
	throws IOException {
		for (FieldDef fd : record.getRecordDef().getFields() ) {
			String data = record.getDataMap().get(fd.getName() );
			byte[] b = data.getBytes(
				record.getRecordDef().getEncoding() );
			for (int i = 0; i < fd.getSize(); i += 1) {
				if (i < b.length) {
					out.write(b[i]);
				} else {
					out.write(0x20);
				}
			}
		}
	}
}
