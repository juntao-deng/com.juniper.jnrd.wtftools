package net.juniper.jmp.persist.cdi;

import javax.enterprise.inject.Produces;

import net.juniper.jmp.persist.IJmpPersistenceManager;
import net.juniper.jmp.persist.JmpPersistenceContext;

/**
 * A cdi bean producer for persistence api
 * @author juntaod
 *
 */
public class PersistenceProducer {
	@Produces
	public IJmpPersistenceManager getJmpPersistence() {
		return JmpPersistenceContext.getInstance();
	}
	
	@Produces @JdmPersistence
	public IJmpPersistenceManager getJdmPersistence(){
		return JmpPersistenceContext.getInstance("jdm");
	}
}
