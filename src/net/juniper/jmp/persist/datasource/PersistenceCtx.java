package net.juniper.jmp.persist.datasource;

/**
 * The context for Persistence API. The context variables is also for children threads.
 * @author juntaod
 *
 */
public final class PersistenceCtx {
	private static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<String>();
	private static final String DEFAULT_DS = "MysqlDS";
	public static String getCurrentDatasource() {
		String ds = threadLocal.get();
		if(ds == null || ds.equals(""))
			ds = DEFAULT_DS;
		return ds;
	}
	
	public static void setCurrentDatasource(String dsName) {
		threadLocal.set(dsName);
	}
}
