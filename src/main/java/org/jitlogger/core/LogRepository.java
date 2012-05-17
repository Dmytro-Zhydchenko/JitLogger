package org.jitlogger.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 
 * Contains a Map implementation for storing log messages in it.
 * 
 * @author Dmytro_Zhydchenko
 * 
 */

public class LogRepository {

	static Map<String, List<Log>> map;

	private static final String DEFAULT_FILE_DEST = "C:/testLog4jTest.log";

	private static String fileDest;

	public void setFileDest(String fileDest) {
		LogRepository.fileDest = fileDest;
	}

	public enum Level {
		DEBUG, INFO, WARN, ERROR;
	}

	/**
	 * 
	 * @param i
	 *            - int primitive representation of the log level
	 * @return String representation of the log level.
	 */
	public static Map<String, List<Log>> getMap() {
		if (map == null) {
			map = new HashMap<String, List<Log>>();
		}
		return map;
	}

	/**
	 * Flushes the information into the file either directly or using log4j in
	 * case if there is a logger for the {@value clazz} value.
	 * 
	 * @param level
	 *            - permissible level of the logs. LogRepository.level value.
	 * @param clazz
	 *            - instance of the Class that includes @Logging annotation.
	 */
	static void flush(Level level, Class<?> clazz) {

		String id = UniqueThreadIdGenerator.getId();

		if (map == null) {
			return;
		}

		List<Log> logs = map.get(id);
		if (logs == null) {
			return;
		}
		Logger logger = getLogger(clazz);

		StringBuilder sb = null;
		if (logger == null) {
			sb = new StringBuilder();
		}

		for (Log log : logs) {
			if (log.level.compareTo(level) < 0) {
				continue;
			}

			if (logger == null) {
				sb.append(log.getLevel()).append(" ");
				sb.append(clazz.getSimpleName()).append(" ");
				sb.append(" - ").append(id).append(" - ");
				sb.append(log.getMessage()).append("\n");
				continue;
			}

			StringBuilder message = new StringBuilder();
			message.append(id).append(" - ").append(log.getMessage());

			switch (log.getLevel()) {
			case DEBUG:
				logger.debug(message.toString());
				break;
			case INFO:
				logger.info(message.toString());
				break;
			case WARN:
				logger.warn(message.toString());
				break;
			case ERROR:
				logger.error(message.toString());
				break;
			}

		}
		if (logger == null) {
			try {
				if (fileDest == null) {
					fileDest = DEFAULT_FILE_DEST;
				}
				writeToFile(fileDest, sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		map.remove(id);
	}

	private static void writeToFile(String fileName, String message)
			throws IOException {
		File file = new File(fileName);
		Writer writer = new FileWriter(file, true);

		BufferedWriter bufWriter = new BufferedWriter(writer);
		bufWriter.write(message);
		bufWriter.flush();

	}

	private static Logger getLogger(Class<?> clazz) {
		String name = clazz.getName();
		int length = name.length();

		Logger logger = null;

		for (int i = length - 1; i >= 0; i = name.lastIndexOf('.', i - 1)) {
			String substr = name.substring(0, i);
			logger = LogManager.exists(substr);
		}

		// the following code might be shared

		// if (logger == null) {
		// logger = LogManager.getRootLogger();
		// }

		return logger;
	}

}
