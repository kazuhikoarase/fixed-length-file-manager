package flfm.scripting;

import java.util.Map;

/**
 * スクリプトインタフェース
 * @author Kazuhiko Arase
 */
public interface IScriptInterface {

	/**
	 * データを読み込む
	 * @param recordFormatFile レコードフォーマットファイル
	 * @return 読み込んだデータ
	 */
	public Map<String, String> readRecord(String recordFormatFile) throws Exception;

	/**
	 * データを読み込む(ファイルの読み取り位置は変わりません)
	 * @param recordFormatFile レコードフォーマットファイル
	 * @return 読み込んだデータ
	 */
	public Map<String, String> peekRecord(String recordFormatFile) throws Exception;
}
