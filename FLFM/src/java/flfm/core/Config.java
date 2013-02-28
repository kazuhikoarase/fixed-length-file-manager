package flfm.core;

public class Config {

	private static final Config instance = new Config();

	public static Config getInstance() {
		return instance;
	}

	private Config() {
	}
	
	public String getAssetsFolderName() {
		return "assets";
	}
	public String getResourceEncoding() {
		return "MS932";
	}
}