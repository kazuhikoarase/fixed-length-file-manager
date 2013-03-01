package flfm.core;

public class Config {

	private static final Config instance = new Config();

	public static Config getInstance() {
		return instance;
	}

	private Config() {
	}
	
	public String getSystemFolderName() {
		return "flfm";
	}
	
	public String getResourceEncoding() {
		return "MS932";
	}
}