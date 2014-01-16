package net.juniper.jmp.persist.jta;

public class JtaSupportFactory {
	private static JtaSupport instance;
	public static JtaSupport getJtaSupport() {
		if(instance == null){
			synchronized(JtaSupportFactory.class){
				if(instance == null){
					instance = new JBossAppServerJtaSupport();
				}
			}
		}
		return instance;
	}
}
