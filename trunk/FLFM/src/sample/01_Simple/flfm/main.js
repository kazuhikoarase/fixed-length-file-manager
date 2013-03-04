//
// 固定長ファイルを読み込むスクリプトです。
//
!function() {
    while(!si.isEOF() ) {
        si.readRecord('format.txt');
    }
}();
