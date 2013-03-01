package flfm.scripting;

import java.util.Map;

/**
 * �X�N���v�g�C���^�t�F�[�X
 * @author Kazuhiko Arase
 */
public interface IScriptInterface {

	/**
	 * �f�[�^�̓ǂݍ��݂����݂�(�t�@�C���̓ǂݎ��ʒu�͕ς��܂���)
	 * @param formatFile �t�H�[�}�b�g�t�@�C��
	 * @return �ǂݍ��񂾃f�[�^
	 */
	public Map<String, String> tryRead(String formatFile) throws Exception;

	/**
	 * �f�[�^��ǂݍ��݂���
	 * @param formatFile �t�H�[�}�b�g�t�@�C��
	 * @return �ǂݍ��񂾃f�[�^
	 */
	public Map<String, String> read(String formatFile) throws Exception;
}
