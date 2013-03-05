package flfm.scripting;

import java.io.File;
import java.util.Map;

/**
 * スクリプトインタフェース
 * @author Kazuhiko Arase
 */
public interface IScriptInterface {

	/**
	 * バイト数を指定して文字列(ISO-8859-1)を読み込む
	 * @param lengthInBytes バイト数
	 * @return 読み込んだ文字列
	 */
	String readString(int lengthInBytes) throws Exception;

	/**
	 * バイト数を指定して文字列(ISO-8859-1)を読み込む(ファイルの読み取り位置は変わりません)
	 * @param lengthInBytes バイト数
	 * @return 読み込んだ文字列
	 */
	String peekString(int lengthInBytes) throws Exception;

	/**
	 * 指定されたフォーマットのレコードを読み込む
	 * @param recordFormatFile レコードフォーマットファイル
	 * @return 読み込んだデータ
	 */
	Map<String, String> readRecord(String recordFormatFile) throws Exception;

	/**
	 * 指定されたフォーマットのレコードを読み込む(ファイルの読み取り位置は変わりません)
	 * @param recordFormatFile レコードフォーマットファイル
	 * @return 読み込んだデータ
	 */
	Map<String, String> peekRecord(String recordFormatFile) throws Exception;

	/**
	 * ファイルサイズを取得する。
	 * @return
	 */
	long getLength() throws Exception;

	/**
	 * 読み取り位置を取得する
	 * @return 読み取り位置
	 */
	long getPosition() throws Exception;
	
	/**
	 * 読み取り位置を設定する
	 * @param position 読み取り位置
	 */
	void setPosition(long position) throws Exception;

	/**
	 * ファイルの終端かどうかを取得する。
	 * @return
	 */
	boolean isEOF() throws Exception;
	
	/**
	 * 現在のデータファイルを取得する。
	 * @return
	 */
	File getDataFile() throws Exception;
	
	/**
	 * スクリプトを実行する
	 * @param src ソース
	 * @return 実行結果
	 */
	Object evalfile(String src) throws Exception;

	/**
	 * メッセージを出力する
	 * @param msg メッセージ
	 */
	void trace(Object msg) throws Exception;
	
	void beginNest();
	void endNest();
}
