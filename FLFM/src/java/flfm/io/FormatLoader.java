package flfm.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import flfm.core.Config;
import flfm.core.Util;
import flfm.model.FieldDef;
import flfm.model.RecordDef;

/**
 * FormatLoader
 * @author Kazuhiko Arase
 */
public class FormatLoader {

	public FormatLoader() {
	}

	public RecordDef load(File file) throws Exception {

		BufferedReader in = new BufferedReader(
			new InputStreamReader(new FileInputStream(file), 
				Config.getInstance().getResourceEncoding() ) );
		try {

			// フォーマット読み込み
			String firstLine = in.readLine();
			ScriptEngineManager manager = new ScriptEngineManager();
	        ScriptEngine engine = manager.getEngineByName("ECMAScript");
	        engine.eval("var opts = " + firstLine);
			int nameIndex = ( (Double)engine.eval("opts.name") ).intValue();
			int commentIndex = ( (Double)engine.eval("opts.comment") ).intValue();
			int typeIndex = ( (Double)engine.eval("opts.type") ).intValue();
			int sizeIndex = ( (Double)engine.eval("opts.size") ).intValue();
			//TODO
			String encoding = Config.getInstance().getResourceEncoding();
			
			List<FieldDef> fields = new ArrayList<FieldDef>();

			String line;
			while ( (line = in.readLine() ) != null) {
				line = Util.rtrim(line);
				if (line.length() == 0) {
					continue;
				}
				String[] cols = Util.strictSplit(line, '\t');
				FieldDef fd = new FieldDef();
				fd.setName(getColumn(cols, nameIndex) );
				fd.setComment(getColumn(cols, commentIndex) );
				fd.setType(getColumn(cols, typeIndex) );
				fd.setSize(Integer.parseInt(getColumn(cols, sizeIndex) ) );
				fields.add(fd);
			}
		
			RecordDef rd = new RecordDef();
			rd.setName(file.getName() );
			rd.setEncoding(encoding);
			rd.setFields(fields);
			return rd;

		}finally {
			in.close();
		}
	}
	
	private String getColumn(String[] cols, int column) {
		return (column < cols.length)? cols[column] : "";
	}
}
