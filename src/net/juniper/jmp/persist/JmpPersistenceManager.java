package net.juniper.jmp.persist;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.Synchronization;

import net.juniper.jmp.persist.IJmpPersistence;
import net.juniper.jmp.persist.IReleaseCallback;
import net.juniper.jmp.persist.JmpTransactionSynchronization;
import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.exp.JmpDbRuntimeException;
import net.juniper.jmp.persist.impl.EntityPersistenceImpl;
import net.juniper.jmp.persist.jta.JtaSupport;
import net.juniper.jmp.persist.jta.JtaSupportFactory;


public final class JmpPersistenceManager{
	private static ThreadLocal<PersistenceWrapper> threadLocal = new ThreadLocal<PersistenceWrapper>();
    public static IJmpPersistence getInstance() {
    	return getInstance(null);
    }
    
	public static IJmpPersistence getInstance(String dsName){
		try{
	    	IJmpPersistence instance = null;
	    	JtaSupport jtaSupport = JtaSupportFactory.getJtaSupport();
	    	if(jtaSupport.hasTransaction()){
	    		Object id = jtaSupport.getTransactionId();
	    		instance = getTxInstance(id, dsName);
	    		
	    		//ensure this transaction has a proper listener
	    		PersistenceWrapper wrapper = getPersistenceWrapper();
	    		if(wrapper.getSynchronization() == null){
	    			Synchronization sync = new JmpTransactionSynchronization();
	    			wrapper.setSynchronization(sync);
	    			jtaSupport.registerSynchronization(sync);
	    		}
	    	}
	    	else{
	    		instance = getNoTxInstance(dsName);
	    	}
	    	return instance;
    	}
    	catch(Exception e){
    		throw new JmpDbRuntimeException(e.getMessage(), e);
    	}
    }
	
	private static IJmpPersistence getNoTxInstance(String dsName) throws JmpDbException {
		PersistenceWrapper wrapper = getPersistenceWrapper();
		IJmpPersistence persistence = wrapper.getJmpNoTxPersistence(dsName);
		if(persistence == null){
			persistence = new EntityPersistenceImpl(dsName, ReleaseCallback.getInstance());
			wrapper.addJmpNoTxPersistence(persistence);
		}
		return persistence;
	}
	
	private static PersistenceWrapper getPersistenceWrapper() {
		PersistenceWrapper wrapper = threadLocal.get();
		if(wrapper == null){
			wrapper = new PersistenceWrapper();
			threadLocal.set(wrapper);
		}
		return wrapper;
	}
	
	
	private static IJmpPersistence getTxInstance(Object id, String dsName) throws JmpDbException {
		PersistenceWrapper wrapper = getPersistenceWrapper();
		IJmpPersistence persistence = wrapper.getJmpTxPersistence(dsName, id);
		if(persistence == null){
			persistence = new EntityPersistenceImpl(dsName, ReleaseCallback.getInstance());
			wrapper.addJmpTxPersistence(id, persistence);
		}
		return persistence;
	}

	public static void releaseTxPersistence() {
		
	}

	protected static void deregisterPersistence(IJmpPersistence persistence) {
		PersistenceWrapper wrapper = threadLocal.get();
		if(wrapper == null)
			throw new JmpDbRuntimeException("shouldn't come here");
		wrapper.removePersistence(persistence);
	}
	
}
class PersistenceWrapper{
	private Map<String, Map<Object, IJmpPersistence>> dsMap = new ConcurrentHashMap<String, Map<Object, IJmpPersistence>>(2);
	private static final String EMPTY_DS_KEY = "$EMPTY_DS_KEY";
	private static final String EMPTY_TX_KEY = "$EMPTY_TX_KEY";
	private Map<String, IJmpPersistence> noTxPersistenceMap;
	private Synchronization synchronization;
	public void setSynchronization(Synchronization sync) {
		this.synchronization = sync;
	}
	public Synchronization getSynchronization() {
		return synchronization;
	}
	
	public void addJmpTxPersistence(Object id, IJmpPersistence jmpPersistence) {
		String dsName = jmpPersistence.getDataSourceName();
		Map<Object, IJmpPersistence> txPersistenceMap = dsMap.get(dsName);
		if(txPersistenceMap == null){
			synchronized(dsMap){
				txPersistenceMap = dsMap.get(dsName);
				if(txPersistenceMap == null){
					txPersistenceMap = new ConcurrentHashMap<Object, IJmpPersistence>(2);
					dsMap.put(dsName, txPersistenceMap);
				}
			}
		}
		txPersistenceMap.put(id, jmpPersistence);
	}
	
	public IJmpPersistence getJmpTxPersistence(String dsName, Object id){
		if(dsName == null || dsName.equals(""))
			dsName = EMPTY_TX_KEY;
		Map<Object, IJmpPersistence> txPersistenceMap = dsMap.get(dsName);
		if(txPersistenceMap == null)
			return null;
		return txPersistenceMap.get(id);
	}
	
	public void addJmpNoTxPersistence(IJmpPersistence jmpPersistence) {
		if(noTxPersistenceMap == null){
			synchronized(dsMap){
				if(noTxPersistenceMap == null){
					noTxPersistenceMap = new ConcurrentHashMap<String, IJmpPersistence>(2);
				}
			}
		}
		noTxPersistenceMap.put(jmpPersistence.getDataSourceName(), jmpPersistence);
	}
	
	public IJmpPersistence getJmpNoTxPersistence(String dsName){
		if(dsName == null || dsName.equals(""))
			dsName = EMPTY_DS_KEY;
		if(noTxPersistenceMap == null)
			return null;
		return noTxPersistenceMap.get(dsName);
	}
	
	public void removePersistence(IJmpPersistence persistence) {
		if(noTxPersistenceMap != null){
			if(noTxPersistenceMap.values().remove(persistence))
				return;
		}
		Map<Object, IJmpPersistence> txPersistenceMap = dsMap.get(persistence.getDataSourceName());
		if(txPersistenceMap != null){
			if(txPersistenceMap.values().remove(persistence))
				return;
		}
		throw new JmpDbRuntimeException("should not come here");
	}
}


class ReleaseCallback implements IReleaseCallback{
	private static final ReleaseCallback instance = new ReleaseCallback();
	private ReleaseCallback() {
	}
	
	public static ReleaseCallback getInstance() {
		return instance;
	}
	
	@Override
	public void callback(IJmpPersistence persistence) {
		JmpPersistenceManager.deregisterPersistence(persistence);
	}
}
