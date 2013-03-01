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

	/**
	 * ファイルサイズを取得する。
	 * @return
	 */
	public long getLength() throws Exception;

	/**
	 * 読み込み位置を取得する
	 * @return 読み込み位置
	 */
	public long getPosition() throws Exception;
	
	/**
	 * 読み込み位置を設定する
	 * @param position 読み込み位置
	 */
	public void setPosition(long position) throws Exception;

	/**
	 * ファイルの終端かどうかを取得する。
	 * @return
	 */
	public boolean isEOF() throws Exception;
}
