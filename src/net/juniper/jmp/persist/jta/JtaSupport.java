package net.juniper.jmp.persist.jta;

import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
/**
 * Jta transaction support api. Reference Hibernate's implementation
 * @author Juntao
 *
 */
public interface JtaSupport {
	
	public TransactionManager retrieveTransactionManager();

	public UserTransaction retrieveUserTransaction();

	public void registerSynchronization(Synchronization synchronization);

	public int getCurrentStatus() throws SystemException;
	
	public boolean hasTransaction() throws SystemException;
	
	public Object getTransactionId() throws SystemException;
}
