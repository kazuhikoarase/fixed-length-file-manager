//
// 固定長ファイルを読み込むスクリプトです。
//
!function() {
	var s = si.peekString(1);
	trace(s);
    si.readRecord('ヘッダ.txt');
    si.beginNest();
    si.readRecord('データ1.txt');
    si.beginNest();
    si.readRecord('データ1.txt');
    si.beginNest();
	si.readRecord('データ2.txt');
    si.readRecord('データ1.txt');
    si.endNest();
    si.endNest();
    si.endNest();
    si.readRecord('フッタ.txt');
}();
