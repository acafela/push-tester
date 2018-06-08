package push.simple.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PushProperties {
	
	private Properties props = new Properties();
	
	PushProperties(String propFilePath) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(propFilePath).getFile());
		FileInputStream fis = new FileInputStream(file);
		props.load(new BufferedInputStream(fis));
	}
	
	String getProperty(String key) {
		return props.getProperty(key);
	}

}
