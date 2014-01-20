package net.juniper.jmp.persist.jta;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * Jta support for jboss, from hibernate's implementations
 * @author Jta
 *
 */
public class JBossAppServerJtaSupport extends AbstractJtaSupport {
	public static final String AS7_TM_NAME = "java:jboss/TransactionManager";
	public static final String AS4_TM_NAME = "java:/TransactionManager";
	public static final String JBOSS__UT_NAME = "java:jboss/UserTransaction";
	public static final String UT_NAME = "java:comp/UserTransaction";
	private Context jndiContext;
	
	@Override
	protected boolean canCacheUserTransaction() {
		return true;
	}

	@Override
	protected boolean canCacheTransactionManager() {
		return true;
	}

	@Override
	protected TransactionManager locateTransactionManager() {
		try {
			return (TransactionManager) getJndiContext().lookup(AS7_TM_NAME);
		}
		catch (NamingException jndiException) {
			try {
				return (TransactionManager) getJndiContext().lookup(AS4_TM_NAME);
			}
			catch (NamingException jndiExceptionInner) {
				return null;
			}
		} 
	}

	@Override
	protected UserTransaction locateUserTransaction() {
		try {
			return (UserTransaction) getJndiContext().lookup(JBOSS__UT_NAME);
		}
		catch (NamingException jndiException) {
			try {
				return (UserTransaction) getJndiContext().lookup(UT_NAME);
			}
			catch (NamingException jndiExceptionInner) {
				return null;
			}
		}
	}
	
	private Context getJndiContext() throws NamingException {
		if(jndiContext == null){
			jndiContext = new InitialContext();
		}
		return jndiContext;
	}
}
