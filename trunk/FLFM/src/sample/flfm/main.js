//
// 固定長ファイルを読み込むスクリプトです。
//

!function() {

	// レコード
	var record;
	
	// ヘッダを読み込む
	si.readRecord('ヘッダ.txt');
	
	// データ x 複数レコード(可変長) + フッタを読み込む
	while(true) {
	
		// とりあえずフッタとして読み込み
		// ※ read ではなく peek を使用して、読み取り位置が変わらないようにする。
		record = si.peekRecord('フッタ.txt');

		// ファイル種別を取得
		var fileType = record.get('FILE_TYPE');
		
		if (fileType == 'D') {
	
			// ※ 同じレコード長のフォーマットを、任意の項目の値によって切り替える例です。
			
			// データ部の場合
			// peek で与信
			record = si.peekRecord('データ1.txt');
			
			// 顧客コードによりフォーマットを切り替える
			if (record.get('CUST_CD') != '00000000') {
				si.readRecord('データ1.txt');
			} else {
				si.readRecord('データ2.txt');
			}
			
		} else if (fileType == 'F') {
		
			// フッタを読み込んで終了
			si.readRecord('フッタ.txt');
			break;
	
		} else {
			throw 'illegal type:' + fileType; 
		}
	}
}();
