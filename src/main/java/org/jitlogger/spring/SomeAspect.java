package org.jitlogger.spring;

import java.util.ArrayList;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.jitlogger.core.Log;
import org.jitlogger.core.UniqueThreadIdGenerator;
import org.jitlogger.core.LogRepository;
import org.jitlogger.core.Logger;

/**
 * 
 * The class is required for logging some information after the specific
 * transaction ends execution rather then immediately. It is thread safe. The
 * unique number is generated for every new thread. Thread safety is achieved by
 * using instance of the ThreadLocal class. Before transaction begins it writes
 * down log messages into the Map implementation and after transaction ends it
 * flushes these log messages into the file. Depending on either with or without
 * exception method ends its execution the specific log messages' threshold is
 * set up.
 * 
 * @author Dmytro_Zhydchenko
 * 
 */

@Aspect
public class SomeAspect {

	private static Logger log = Logger.getLogger();

	/**
	 * The method is called before the method annotated by the Logging
	 * annotation returns normally. It allocates some space for writing down log
	 * messages.
	 * 
	 * @param jp
	 *            - instance of the JointPoint implementation
	 * @param logging
	 *            - instance of the Logging annotation
	 */
	@Before("@annotation(logging)")
	public void logBefore(JoinPoint jp, Logging logging) {

		UniqueThreadIdGenerator.setName(logging.name());
		String transactionId = UniqueThreadIdGenerator.getId();

		LogRepository.getMap().put(transactionId, new ArrayList<Log>());

	}

	/**
	 * The method is called immediately after the method execution annotated by
	 * the Logging annotation returns normally. It uses LogRepository.flush()
	 * static method to write log messages into the file. In this case log
	 * messages' threshold is "INFO".
	 * 
	 * @param jp
	 *            - instance of the JointPoint implementation
	 * @param logging
	 *            - instance of the Logging annotation
	 * 
	 */
	@AfterReturning("@annotation(logging)")
	public void logAfterReturning(JoinPoint jp, Logging logging) {
		// Class<?> clazz = jp.getTarget().getClass();
		// log.flush(LogRepository.INFO, clazz);
		log.flush(LogRepository.INFO);
	}

	/**
	 * The method is called immediately after the method execution annotated by
	 * the Logging annotation exits by throwing an exception. It uses
	 * LogRepository.flush() static method to write log messages into the file.
	 * In this case log messages' threshold is "DEBUG".
	 * 
	 * @param jp
	 *            - instance of the JointPoint implementation
	 * @param logging
	 *            - instance of the Logging annotation
	 */
	@AfterThrowing("@annotation(logging)")
	public void logAfterThrowing(JoinPoint jp, Logging logging) {
		// Class<?> clazz = jp.getTarget().getClass();
		// log.flush(LogRepository.DEBUG, clazz);
		log.flush(LogRepository.DEBUG);
	}

}
