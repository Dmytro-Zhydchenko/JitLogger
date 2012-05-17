package org.jitlogger.core;

import org.jitlogger.core.LogRepository.Level;

/**
 * Contains message of the log and its threshold level.
 * 
 * @author Dmytro_Zhydchenko
 * 
 */
public class Log {

	@Deprecated
	String className;

	Level level;

	String message;

	public Log() {

	}

	public Log(String className, Level level, String message) {
		this.className = className;
		this.level = level;
		this.message = message;
	}

	public String getClassName() {
		return className;
	}

	@Deprecated
	public void setClassName(String className) {
		this.className = className;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Log [className=" + className + ", level=" + level
				+ ", message=" + message + "]";
	}

}
