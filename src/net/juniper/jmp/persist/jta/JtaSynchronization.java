package net.juniper.jmp.persist.jta;

import javax.transaction.Synchronization;
/**
 * a transaction callback  
 * @author Juntao
 *
 */
public class JtaSynchronization implements Synchronization {

	@Override
	public void afterCompletion(int status) {

	}

	@Override
	public void beforeCompletion() {
	}

}
