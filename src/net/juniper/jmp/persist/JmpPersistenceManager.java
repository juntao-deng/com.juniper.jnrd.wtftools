package net.juniper.jmp.persist;

import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.exp.JmpDbRuntimeException;
import net.juniper.jmp.persist.impl.EntityPersistenceImpl;


public abstract class JmpPersistenceManager {
    public static IJmpPersistence getInstance() {
        try {
			return new EntityPersistenceImpl();
		} 
        catch (JmpDbException e) {
			throw new JmpDbRuntimeException(e);
		}
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