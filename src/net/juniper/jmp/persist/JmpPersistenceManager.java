package net.juniper.jmp.persist;

import java.util.HashMap;
import java.util.Map;

import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.exp.JmpDbRuntimeException;
import net.juniper.jmp.persist.impl.EntityPersistenceImpl;
import net.juniper.jmp.persist.jta.JtaSupportFactory;


public abstract class JmpPersistenceManager {
	private static ThreadLocal<Map<String, IJmpPersistence>> threadLocal = new ThreadLocal<Map<String, IJmpPersistence>>();
	private static Map<String, IJmpPersistence> persistenceMap = new HashMap<String, IJmpPersistence>();
    public static IJmpPersistence getInstance() {
    	IJmpPersistence instance = null;
    	if(JtaSupportFactory.getJtaSupport().hasTransaction()){
    		Object id = JtaSupportFactory.getJtaSupport().getTransactionId();
    		instance = getNoTxInstance(id, null);
    	}
    	else{
    		instance = getNoTxInstance(null);
    	}
    	return instance;
    }
    
    
    private static IJmpPersistence getNoTxInstance(String dsName) {
		return null;
	}


	private static IJmpPersistence getNoTxInstance(Object id, String dsName) {
		return null;
	}

	public static IJmpPersistence getInstance(String dsName){
        try {
			return new EntityPersistenceImpl(dsName);
		} 
        catch (JmpDbException e) {
        	throw new JmpDbRuntimeException(e);
        }
    }
}