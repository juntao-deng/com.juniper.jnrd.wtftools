package net.juniper.jmp.persist.cdi;

import javax.enterprise.inject.Produces;

import net.juniper.jmp.persist.IJmpPersistence;
import net.juniper.jmp.persist.JmpPersistenceManager;

/**
 * A cdi bean producer for persistence api
 * @author juntaod
 *
 */
public class PersistenceProducer {
	@Produces
	public IJmpPersistence getJmpPersistence() {
		return JmpPersistenceManager.getInstance();
	}
	
	@Produces @JdmPersistence
	public IJmpPersistence getJdmPersistence(){
		return JmpPersistenceManager.getInstance("jdm");
	}
}
