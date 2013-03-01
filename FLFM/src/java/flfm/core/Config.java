package flfm.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Config
 * @author Kazuhiko Arase
 */
public class Config {

	private static final Config instance = new Config();

	public static Config getInstance() {
		return instance;
	}

	private static final String CURRENT_FOLDER = "current-folder";
	
	private Properties props;
	
	private Config() {
		props = new Properties();
		try {
			InputStream in = new BufferedInputStream(
					new FileInputStream(getConfigFile() ) );
			try {
				props.load(in);
			} finally {
				in.close();
			}
		} catch(Exception e) {
			// ignore
		}
	}
	
	public void store() {
		try {
			OutputStream out = new BufferedOutputStream(
					new FileOutputStream(getConfigFile() ) );
			try {
				props.store(out, "");
			} finally {
				out.close();
			}
		} catch(Exception e) {
			// ignore
		}
	}

	public void setCurrentFolder(String folder) {
		props.setProperty(CURRENT_FOLDER, folder);
	}

	public String getCurrentFolder() {
		return props.getProperty(CURRENT_FOLDER);
	}
	
	private String getUserHome() {
		return System.getProperty("user.home");
	}
	
	private String getConfigFile() {
		return new File(getUserHome(), "flfm.properties").getAbsolutePath();
	}
	
	public String getSystemFolderName() {
		return "flfm";
	}
	
	public String getResourceEncoding() {
		return "MS932";
	}
}