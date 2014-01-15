package net.juniper.jmp.persist.jta;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.service.jta.platform.spi.JtaPlatformException;

/**
 * from hibernate's implementation
 * @author Juntao
 *
 */
public class JbossStandAloneJtaSupport extends AbstractJtaSupport {
	private static final String JBOSS_TM_CLASS_NAME = "com.arjuna.ats.jta.TransactionManager";
	private static final String JBOSS_UT_CLASS_NAME = "com.arjuna.ats.jta.UserTransaction";

	@Override
	protected TransactionManager locateTransactionManager() {
		try {
			final Class jbossTmClass = Class.forName(JBOSS_TM_CLASS_NAME);
			return (TransactionManager) jbossTmClass.getMethod("transactionManager").invoke( null );
		}
		catch ( Exception e ) {
			throw new JtaPlatformException( "Could not obtain JBoss Transactions transaction manager instance", e );
		}
	}

	@Override
	protected UserTransaction locateUserTransaction() {
		try {
			final Class jbossUtClass = Class.forName(JBOSS_UT_CLASS_NAME);
			return (UserTransaction) jbossUtClass.getMethod("userTransaction").invoke(null);
		}
		catch ( Exception e ) {
			throw new JtaPlatformException( "Could not obtain JBoss Transactions user transaction instance", e );
		}
	}

}
