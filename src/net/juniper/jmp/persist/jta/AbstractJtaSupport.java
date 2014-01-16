package net.juniper.jmp.persist.jta;

import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import net.juniper.jmp.persist.exp.JmpDbRuntimeException;

/**
 * Base class for jta support
 * @author Juntao
 *
 */
public abstract class AbstractJtaSupport implements JtaSupport{
	
	private TransactionManager transactionManager;

	private UserTransaction userTransaction;
	
	@Override
	public TransactionManager retrieveTransactionManager() {
		if(canCacheTransactionManager()) {
			if(transactionManager == null) {
				transactionManager = locateTransactionManager();
			}
			return transactionManager;
		}
		else {
			return locateTransactionManager();
		}
	}

	protected boolean canCacheTransactionManager() {
		return true;
	}
	
	protected boolean canCacheUserTransaction() {
		return false;
	}

	@Override
	public UserTransaction retrieveUserTransaction() {
		if (canCacheUserTransaction()) {
			if ( userTransaction == null ) {
				userTransaction = locateUserTransaction();
			}
			return userTransaction;
		}
		return locateUserTransaction();
	}
	

	protected abstract TransactionManager locateTransactionManager();
	protected abstract UserTransaction locateUserTransaction();
	
	@Override
	public void registerSynchronization(Synchronization synchronization) {
		try{
			retrieveTransactionManager().getTransaction().registerSynchronization( synchronization );
		}
		catch(Exception e){
			throw new JmpDbRuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public int getCurrentStatus() throws SystemException {
		return retrieveTransactionManager().getStatus();
	}
}
