package org.jitlogger.core;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import org.jitlogger.core.LogRepository.Level;

/**
 * 
 * Returns an instance of this class and writes logs down into the Map
 * implementation.
 * 
 * @author Dmytro_Zhydchenko
 * 
 */
public class Logger {

	private static Logger logger;

	private static Class<?> clazz;

	private UncaughtExceptionHandler uncaughtExceptionHandler = new MyUncaughtExceptionHandler();

	private Logger() {
	}

	/**
	 * 
	 * @return new instance of the Logger in case if there is no instance or
	 *         existing instance in another case.
	 */
	public static Logger getLogger(Class<?> clazz) {
		if (logger == null) {
			logger = new Logger();
		}
		Logger.clazz = clazz;
		return logger;
	}

	/**
	 * 
	 * @return Logger instance. Return null if there is no such instance.
	 */
	public static Logger getLogger() {
		return logger;
	}

	public void debug(String message) {
		this.addLog(Level.DEBUG, message);
	}

	public void info(String message) {
		this.addLog(Level.INFO, message);
	}

	public void warn(String message) {
		this.addLog(Level.WARN, message);
	}

	public void error(String message) {
		this.addLog(Level.ERROR, message);
	}

	private void addLog(Level level, String message) {

		if (!Thread.currentThread().getUncaughtExceptionHandler()
				.equals(uncaughtExceptionHandler)) {
			Thread.currentThread().setUncaughtExceptionHandler(
					uncaughtExceptionHandler);
		}

		String id = UniqueThreadIdGenerator.getId();
		List<Log> messages = LogRepository.getMap().get(id);

		// The following check might be removing
		if (messages == null) {
			messages = new ArrayList<Log>();
			LogRepository.getMap().put(id, messages);
		}

		Log logBean = new Log();
		logBean.setLevel(level);
		logBean.setMessage(message);

		messages.add(logBean);
	}

	/**
	 * Writes data into the file according to level value.
	 * 
	 * @param level
	 *            LogRepository.'level' value.
	 * @param clazz
	 *            class instance
	 */
	public void flush(Level level) {

		LogRepository.flush(level, clazz);
	}

	/**
	 * Writes data into file with the DEBUG level.
	 * 
	 * @param clazz
	 *            class instance
	 */
	public void flush() {
		LogRepository.flush(Level.DEBUG, clazz);
	}

	@Deprecated
	public void setUncaughtExceptionHandler(
			UncaughtExceptionHandler uncaughtExceptionHandler) {
		Thread.currentThread().setUncaughtExceptionHandler(
				uncaughtExceptionHandler);
	}

	private static class MyUncaughtExceptionHandler implements
			Thread.UncaughtExceptionHandler {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			System.err.println("Uncaught exception by " + t + ":");
			Logger log = Logger.getLogger();
			if (log != null) {
				log.flush(Level.DEBUG);
			}
			e.printStackTrace();
		}

	}

}
