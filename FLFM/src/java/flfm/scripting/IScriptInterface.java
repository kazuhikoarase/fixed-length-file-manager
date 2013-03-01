package flfm.scripting;

import java.util.Map;

/**
 * �X�N���v�g�C���^�t�F�[�X
 * @author Kazuhiko Arase
 */
public interface IScriptInterface {

	/**
	 * �f�[�^��ǂݍ���
	 * @param recordFormatFile ���R�[�h�t�H�[�}�b�g�t�@�C��
	 * @return �ǂݍ��񂾃f�[�^
	 */
	public Map<String, String> readRecord(String recordFormatFile) throws Exception;

	/**
	 * �f�[�^��ǂݍ���(�t�@�C���̓ǂݎ��ʒu�͕ς��܂���)
	 * @param recordFormatFile ���R�[�h�t�H�[�}�b�g�t�@�C��
	 * @return �ǂݍ��񂾃f�[�^
	 */
	public Map<String, String> peekRecord(String recordFormatFile) throws Exception;

	/**
	 * �t�@�C���T�C�Y���擾����B
	 * @return
	 */
	public long getLength() throws Exception;

	/**
	 * �ǂݍ��݈ʒu���擾����
	 * @return �ǂݍ��݈ʒu
	 */
	public long getPosition() throws Exception;
	
	/**
	 * �ǂݍ��݈ʒu��ݒ肷��
	 * @param position �ǂݍ��݈ʒu
	 */
	public void setPosition(long position) throws Exception;

	/**
	 * �t�@�C���̏I�[���ǂ������擾����B
	 * @return
	 */
	public boolean isEOF() throws Exception;
}
