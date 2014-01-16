package net.juniper.jmp.persist.utils;

import org.jboss.resteasy.logging.Logger;
/**
 * SqlLogger, prevent from creating too much instances of logger.
 * It will log to default inherited logger and also to special sqllog.log
 * @author juntaod
 *
 */
public class SqlLogger {
	private static Logger logger = Logger.getLogger(SqlLogger.class);
	public static void error(String msg, Throwable e){
		logger.error(msg, e);
	}
	public static void warn(String msg) {
		logger.warn(msg);
	}
	public static void logTranslator(long l, String srcSql, String result) {
		
	}
	public static boolean isLogTranslatorEnabled() {
		return false;
	}
}
