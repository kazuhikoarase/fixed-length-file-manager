//
// �Œ蒷�t�@�C����ǂݍ��ރX�N���v�g�ł��B
//
!function() {
	var s = si.peekString(1);
	trace(s);
    si.readRecord('�w�b�_.txt');
    si.beginNest();
    var r = si.readRecord('�f�[�^1.txt');
    r.put('__RECORD_NAME__', '�J�X�^�����R�[�h��');
    si.beginNest();
    si.readRecord('�f�[�^1.txt');
    si.beginNest();
	si.readRecord('�f�[�^2.txt');
    si.readRecord('�f�[�^1.txt');
    si.endNest();
    si.endNest();
    si.endNest();
    si.readString(2);
    si.readRecord('�t�b�^.txt');
}();
