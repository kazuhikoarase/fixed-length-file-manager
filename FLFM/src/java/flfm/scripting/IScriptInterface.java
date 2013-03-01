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
}
