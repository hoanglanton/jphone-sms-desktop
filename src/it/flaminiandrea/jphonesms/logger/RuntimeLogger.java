package it.flaminiandrea.jphonesms.logger;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RuntimeLogger {

	private static RuntimeLogger instance = null;
	private HashMap<String, Logger> loggers = new HashMap<String, Logger>();
	private static String LOGGER_CONF_PROPERTIES_FILE_PATH = "conf/logs-conf.properties";
	
	protected RuntimeLogger() {
		// Exists only to defeat instantiation.
	}
	
	public static RuntimeLogger getInstance() {
		if(instance == null) {
			instance = new RuntimeLogger();
		}
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	public Logger getLogger(Class className) {
		PropertyConfigurator.configure(getLoggerConfigurationFilePath());
		Logger logger = loggers.get(className);
		if (logger == null) {
			logger = Logger.getLogger(className);
			loggers.put(className.getName(), logger);
		}
		return logger;
	}

	private static String getLoggerConfigurationFilePath() {
		String result = LOGGER_CONF_PROPERTIES_FILE_PATH;
		result = result.replace("/", System.getProperty("file.separator"));
		return result;
	}

}
