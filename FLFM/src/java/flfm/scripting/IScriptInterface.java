package flfm.scripting;

import java.util.Map;

/**
 * スクリプトインタフェース
 * @author Kazuhiko Arase
 */
public interface IScriptInterface {

	/**
	 * データの読み込みを試みる(ファイルの読み取り位置は変わりません)
	 * @param formatFile フォーマットファイル
	 * @return 読み込んだデータ
	 */
	public Map<String, String> tryRead(String formatFile) throws Exception;

	/**
	 * データを読み込みこむ
	 * @param formatFile フォーマットファイル
	 * @return 読み込んだデータ
	 */
	public Map<String, String> read(String formatFile) throws Exception;
}
