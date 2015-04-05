# Getting Started #



# システム要件 #

  * Java6以降

# フォルダ構成 #

ファイルの種別毎にフォルダを用意し、その配下に固定長のデータファイルを置きます。

データファイルと同階層に flfm というフォルダを作成し、
フォーマット定義ファイル及び データ読み込み用のスクリプトを置きます。

同階層に flfm が存在しない場合、親フォルダを検索します。

FLFM でデータファイルを開くと、データと同階層の flfm/main.js が実行され、
任意のフォーマットとして読み込まれます。

```
folder/
       data1.txt
       data2.txt
          .
          .
          .
       flfm/
            main.js
            format1.txt
            format2.txt
               .
               .
               .
```

異なる種別のファイルは、別フォルダを用意し、同じような構成で main.js 等を配置します。

# フォーマット定義ファイル #

1行目は 2行目以降のデータの何列目が何のデータなのかを示す定義です(1列目 = 0)。

| name | 項目名(英名) | 必須 |
|:-----|:------------------|:-------|
| comment | コメント(日本語の項目名) | 必須 |
| type | 型 |  |
| size | サイズ(バイト数) | 必須 |
| description | 備考 |  |

2行目以降はタブ区切りのテキストです。
Excel からコピー＆ペーストすると、この形式となります。

```
{name: 21, comment: 1, description: 20, type: 16, size: 17}
1	ファイル種別															Z	1			H:ヘッダ/D:データ/F:フッタ	FILE_TYPE
2	取引先コード															Z	8				CUST_CD
3	取引先名															Z	30				CUST_NAME
4	電話番号															Z	20				TEL
5	住所１															Z	30				ADDR1
6	住所２															Z	30				ADDR2
7	発行日 - 元号															Z	1				ISSUE_G
8	発行日 - 年															Z	2				ISSUE_Y
9	発行日 - 月															Z	2				ISSUE_M
10	発行日 - 日															Z	2				ISSUE_D
```

# データ読み込み用スクリプト #

データ読み込みは[スクリプトインタフェース(si)のAPI](http://fixed-length-file-manager.googlecode.com/svn/assets/apidocs/flfm/scripting/IScriptInterface.html)を使用して、
レコード単位で行います。

| peekRecord(引数：フォーマット定義ファイル名) | ファイル読み取り位置を変えずにデータを読み込む |
|:-------------------------------------------------------------|:----------------------------------------------------------------------|
| readRecord(引数：フォーマット定義ファイル名) | データを読み込む |

通常のレコード読み込みは readRecord です。

下記のスクリプトは、単一のフォーマットのレコードをファイル終端まで読み込みます。

```
!function() {
    // ファイル終端まで、 format.txt で定義されたレコードを読み込む。
    while(!si.isEOF() ) {
        si.readRecord('format.txt');
    }
}();
```

データの中身によって、読み込むフォーマットを振り分けたい場合は
あらかじめ peekRecord で内容を確認して処理を分岐させます。

```
!function() {

    trace('start');

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

    trace('end');

}();
```

# FLFM を使用する #

FLFM.jar をダブルクリックしてアプリケーションを起動します。

ダブルクリックで起動できない場合、標準出力を見たい場合などは
下記のようにコマンドラインから起動します。

```
java -jar FLFM.jar
```

![http://fixed-length-file-manager.googlecode.com/svn/assets/files.png](http://fixed-length-file-manager.googlecode.com/svn/assets/files.png)

![http://fixed-length-file-manager.googlecode.com/svn/assets/FLFM.png](http://fixed-length-file-manager.googlecode.com/svn/assets/FLFM.png)