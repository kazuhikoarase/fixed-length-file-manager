package flfm.core;


/**
 * Util
 * @author Kazuhiko Arase
 */
public class Util {
	private Util() {
	}
	public static String rtrim(String s) {
		if (s == null) return null;
		return s.replaceAll("\\s+$", "");
	}
}