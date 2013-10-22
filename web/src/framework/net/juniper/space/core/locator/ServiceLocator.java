package net.juniper.space.core.locator;

public class ServiceLocator {
	private static ServiceProvider localProvider = new LocalServiceProvider();
	
	public static void getInstance() {
		
	}
	
	public static <T>T getService(Class<T> c){
		return (T) getService(c.getName());
	}
	
	public static Object getService(String c){
		return localProvider.getService(c);
	}
}
