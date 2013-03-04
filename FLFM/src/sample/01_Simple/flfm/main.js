//
// 固定長ファイルを読み込むスクリプトです。
//
!function() {
	// ファイル終端まで、 format.txt で定義されたレコードを読み込む。
    while(!si.isEOF() ) {
        si.readRecord('format.txt');
    }
}();
