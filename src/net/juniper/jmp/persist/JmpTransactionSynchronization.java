package net.juniper.jmp.persist;

import javax.transaction.Synchronization;

public class JmpTransactionSynchronization implements Synchronization {

	@Override
	public void afterCompletion(int arg0) {

	}

	@Override
	public void beforeCompletion() {
		JmpPersistenceContext.releaseTxPersistence();
	}
}
