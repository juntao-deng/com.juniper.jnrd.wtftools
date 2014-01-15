package net.juniper.jmp.persist.datasource;

/**
 * The context for Persistence API. The context variables is also for children threads.
 * @author juntaod
 *
 */
public final class PersistenceCtx {
	private static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<String>();
	public static String getCurrentDatasource() {
		return threadLocal.get();
	}
	
	public static void setCurrentDatasource(String dsName) {
		threadLocal.set(dsName);
	}
}
