package push.simple.service;

import java.io.IOException;
import java.util.Properties;

public class PushProperties {
	
	private Properties props = new Properties();
	
	PushProperties(String propFilePath) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		props.load(classLoader.getResourceAsStream(propFilePath));
	}
	
	String getProperty(String key) {
		return props.getProperty(key);
	}

}
