//
// �Œ蒷�t�@�C����ǂݍ��ރX�N���v�g�ł��B
//

!function() {

	// ���R�[�h
	var record;
	
	// �w�b�_��ǂݍ���
	si.readRecord('�w�b�_.txt');
	
	// �f�[�^ x �������R�[�h(�ϒ�) + �t�b�^��ǂݍ���
	while(true) {
	
		// �Ƃ肠�����t�b�^�Ƃ��ēǂݍ���
		// �� read �ł͂Ȃ� peek ���g�p���āA�ǂݎ��ʒu���ς��Ȃ��悤�ɂ���B
		record = si.peekRecord('�t�b�^.txt');

		// �t�@�C����ʂ��擾
		var fileType = record.get('FILE_TYPE');
		
		if (fileType == 'D') {
	
			// �� �������R�[�h���̃t�H�[�}�b�g���A�C�ӂ̍��ڂ̒l�ɂ���Đ؂�ւ����ł��B
			
			// �f�[�^���̏ꍇ
			// peek �ŗ^�M
			record = si.peekRecord('�f�[�^1.txt');
			
			// �ڋq�R�[�h�ɂ��t�H�[�}�b�g��؂�ւ���
			if (record.get('CUST_CD') != '00000000') {
				si.readRecord('�f�[�^1.txt');
			} else {
				si.readRecord('�f�[�^2.txt');
			}
			
		} else if (fileType == 'F') {
		
			// �t�b�^��ǂݍ���ŏI��
			si.readRecord('�t�b�^.txt');
			break;
	
		} else {
			throw 'illegal type:' + fileType; 
		}
	}
}();
