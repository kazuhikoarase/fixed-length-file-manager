package flfm.scripting;

import java.io.File;
import java.util.Map;

/**
 * �X�N���v�g�C���^�t�F�[�X
 * @author Kazuhiko Arase
 */
public interface IScriptInterface {

	/**
	 * �o�C�g�����w�肵�ĕ�����(ISO-8859-1)��ǂݍ��ށB
	 * @param lengthInBytes �o�C�g��
	 * @return �ǂݍ��񂾕�����
	 */
	String readString(int lengthInBytes) throws Exception;

	/**
	 * �o�C�g�����w�肵�ĕ�����(ISO-8859-1)��ǂݍ���(�t�@�C���̓ǂݎ��ʒu�͕ς��܂���)�B
	 * @param lengthInBytes �o�C�g��
	 * @return �ǂݍ��񂾕�����
	 */
	String peekString(int lengthInBytes) throws Exception;

	/**
	 * �w�肳�ꂽ�t�H�[�}�b�g�̃��R�[�h��ǂݍ��ށB
	 * @param recordFormatFile ���R�[�h�t�H�[�}�b�g�t�@�C��
	 * @return �ǂݍ��񂾃f�[�^
	 */
	Map<String, String> readRecord(String recordFormatFile) throws Exception;

	/**
	 * �w�肳�ꂽ�t�H�[�}�b�g�̃��R�[�h��ǂݍ���(�t�@�C���̓ǂݎ��ʒu�͕ς��܂���)�B
	 * @param recordFormatFile ���R�[�h�t�H�[�}�b�g�t�@�C��
	 * @return �ǂݍ��񂾃f�[�^
	 */
	Map<String, String> peekRecord(String recordFormatFile) throws Exception;

	/**
	 * �t�@�C���T�C�Y���擾����B
	 * @return
	 */
	long getLength() throws Exception;

	/**
	 * �ǂݎ��ʒu���擾����B
	 * @return �ǂݎ��ʒu
	 */
	long getPosition() throws Exception;
	
	/**
	 * �ǂݎ��ʒu��ݒ肷��B
	 * @param position �ǂݎ��ʒu
	 */
	void setPosition(long position) throws Exception;

	/**
	 * �t�@�C���̏I�[���ǂ������擾����B
	 * @return
	 */
	boolean isEOF() throws Exception;
	
	/**
	 * ���݂̃f�[�^�t�@�C�����擾����B
	 * @return
	 */
	File getDataFile() throws Exception;
	
	/**
	 * �X�N���v�g�����s����B
	 * @param src �\�[�X
	 * @return ���s����
	 */
	Object evalfile(String src) throws Exception;

	/**
	 * ���b�Z�[�W���o�͂���B
	 * @param msg ���b�Z�[�W
	 */
	void trace(Object msg) throws Exception;

	/**
	 * ���R�[�h�̃l�X�g���J�n����B
	 */
	void beginNest() throws Exception;

	/**
	 * ���R�[�h�̃l�X�g���I������B
	 */
	void endNest() throws Exception;
}
