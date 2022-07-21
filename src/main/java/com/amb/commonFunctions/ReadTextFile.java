package com.amb.commonFunctions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class ReadTextFile {
	static String data = "";
	static String path = getFilePath();

	/**
	 * This method will return absolute path.
	 * @return
	 */
	public static String getFilePath() {
		String filepath = "";
		File file = new File("");
		String absolutePathOfFirstFile = file.getAbsolutePath();
		filepath = absolutePathOfFirstFile.replaceAll("\\\\+", "/");
		return filepath;
	}

	/**
	 * This method will read value from config properties file with the help of key.
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String readApplicationFile(String key) throws Exception {
		String value = "";
		try {
			Properties prop = new Properties();
			File f = new File(ReadTextFile.getFilePath() + "\\config.properties");
			if (f.exists()) {
				prop.load(new FileInputStream(f));
				value = prop.getProperty(key);
				//System.out.println("value " + key);
			} else {
				throw new Exception("File not found");
			}
		} catch (FileNotFoundException ex) {
			System.out.println("Failed to read from application.properties file.");
			throw ex;
		}
		if (value == null)
			throw new Exception("Key not found in properties file");
		return value;
	}


}
