package org.jitlogger.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * The annotation shows where log messages can be used.
 * 
 * @author Dmytro_Zhydchenko
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Logging {

	/**
	 * 
	 * @return value of the transaction name.
	 */
	String name();

}
