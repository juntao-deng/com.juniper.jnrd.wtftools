package net.juniper.jmp.persist.utils;

import org.jboss.resteasy.logging.Logger;

public class SqlLogger {
	private static Logger logger = Logger.getLogger(SqlLogger.class);
	public static void error(String msg, Throwable e){
		logger.error(msg, e);
	}
	public static void warn(String msg) {
		logger.warn(msg);
	}
}
