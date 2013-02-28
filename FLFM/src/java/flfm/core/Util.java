package flfm.core;

import java.util.ArrayList;
import java.util.List;

public class Util {
	private Util() {
	}
	public static String rtrim(String s) {
		if (s == null) return null;
		return s.replaceAll("\\s+$", "");
	}
	
	public static String[] strictSplit(String s, char c) {
		List<String> lines = new ArrayList<String>();
		int start = 0;
		int index;
		while ( (index = s.indexOf(c, start) ) != -1) {
			lines.add(s.substring(start, index) );
			start = index + 1;
		}
		if (start < s.length() ) {
			lines.add(s.substring(start) );
		}
		return lines.toArray(new String[lines.size()]);
	}
}