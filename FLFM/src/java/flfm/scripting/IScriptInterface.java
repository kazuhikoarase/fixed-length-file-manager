package flfm.scripting;

import java.util.Map;

/**
 * �X�N���v�g�C���^�t�F�[�X
 * @author Kazuhiko Arase
 */
public interface IScriptInterface {

	/**
	 * �w�肳�ꂽ�t�H�[�}�b�g�̃��R�[�h��ǂݍ���
	 * @param recordFormatFile ���R�[�h�t�H�[�}�b�g�t�@�C��
	 * @return �ǂݍ��񂾃f�[�^
	 */
	public Map<String, String> readRecord(String recordFormatFile) throws Exception;

	/**
	 * �w�肳�ꂽ�t�H�[�}�b�g�̃��R�[�h��ǂݍ���(�t�@�C���̓ǂݎ��ʒu�͕ς��܂���)
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
	 * �ǂݎ��ʒu���擾����
	 * @return �ǂݎ��ʒu
	 */
	public long getPosition() throws Exception;
	
	/**
	 * �ǂݎ��ʒu��ݒ肷��
	 * @param position �ǂݎ��ʒu
	 */
	public void setPosition(long position) throws Exception;

	/**
	 * �t�@�C���̏I�[���ǂ������擾����B
	 * @return
	 */
	public boolean isEOF() throws Exception;
}
