package org.jitlogger.core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author Dmytro_Zhydchenko
 * 
 *         The class is used for providing transaction id, which has to be
 *         unique to both thread and operation. The id value is responsible for
 *         uniqueness of transaction id name according to specific threaad. The
 *         txName value is responsible for uniqueness of transaction id name
 *         according to currently executing transaction.
 * 
 */
public class UniqueThreadIdGenerator {

	private static final AtomicInteger id = new AtomicInteger(0);

	/**
	 * The following inner class is responsible for thread safety
	 */
	private static final ThreadLocal<String> transactionId = new ThreadLocal<String>() {

		private String txName;

		@Override
		protected String initialValue() {
			return String.valueOf(id.getAndIncrement());
		};

		@Override
		public void set(String value) {
			txName = value;
		}

		@Override
		public String get() {
			return (txName + "-" + id.get());
		}

	};

	/**
	 * 
	 * @return id of the transaction which consists of the transaction name and
	 *         id that unique for a thread
	 */
	public static String getId() {
		String transaction = transactionId.get();
		return transaction;
	}

	/**
	 * 
	 * @param txName
	 *            - name of the executing transaction
	 */
	public static void setName(String txName) {
		transactionId.set(txName);
	}

}
