package net.juniper.jmp.persist;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Synchronization;

import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.exp.JmpDbRuntimeException;
import net.juniper.jmp.persist.impl.EntityPersistenceImpl;
import net.juniper.jmp.persist.jta.JtaSupportFactory;


public final class JmpPersistenceManager{
	private static ThreadLocal<PersistenceWrapper> threadLocal = new ThreadLocal<PersistenceWrapper>();
	private static Map<String, IJmpPersistence> persistenceMap = new HashMap<String, IJmpPersistence>();
    public static IJmpPersistence getInstance() {
    	IJmpPersistence instance = null;
    	if(JtaSupportFactory.getJtaSupport().hasTransaction()){
    		Object id = JtaSupportFactory.getJtaSupport().getTransactionId();
    		instance = getTxInstance(id, null);
    		JtaSupportFactory.getJtaSupport().registerSynchronization(new JmpTransactionSynchronization());
    	}
    	else{
    		instance = getNoTxInstance(null);
    	}
    	return instance;
    }
    
	public static IJmpPersistence getInstance(String dsName){
        try {
			return new EntityPersistenceImpl(dsName);
		} 
        catch (JmpDbException e) {
        	throw new JmpDbRuntimeException(e);
        }
    }
	
	private static IJmpPersistence getNoTxInstance(String dsName) {
		PersistenceWrapper wrapper = getPersistenceWrapper();
		IJmpPersistence persistence = wrapper.getNoTxPersistence(dsName);
		
		return null;
	}
	
	private static PersistenceWrapper getPersistenceWrapper() {
		PersistenceWrapper wrapper = threadLocal.get();
		if(wrapper == null){
			wrapper = new PersistenceWrapper();
			threadLocal.set(wrapper);
		}
		return wrapper;
	}
	
	
	private static IJmpPersistence getTxInstance(Object id, String dsName) {
		PersistenceWrapper wrapper = getPersistenceWrapper();
		if(wrapper.getSynchronization() == null){
			Synchronization sync = new JmpTransactionSynchronization();
			wrapper.setSynchronization(sync);
		}
		return null;
	}

	public static void releaseTxPersistence() {
		
	}
	
}
class PersistenceWrapper{
	private Map<String, IJmpPersistence> txPersistenceMap;
	private Map<String, IJmpPersistence> noTxPersistenceMap;
	private Synchronization synchronization;
	public void setSynchronization(Synchronization sync) {
		this.synchronization = sync;
	}
	public Synchronization getSynchronization() {
		return synchronization;
	}
}
