package net.juniper.jmp.core.ctx;


public class ApiContext {
	private static ThreadLocal<ApiContextInfo> threadLocal = new ThreadLocal<ApiContextInfo>();
	public static void reset() {
		threadLocal.remove();
	}
	public static void init(ApiContextInfo apiContext) {
		threadLocal.set(apiContext);
	}
	
	public static PagingContext getPagingContext() {
		return threadLocal.get().getPageContext();
	}
}
