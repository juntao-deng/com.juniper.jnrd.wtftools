package net.juniper.jmp.persist;

import net.juniper.jmp.persist.exp.JmpDbException;
import net.juniper.jmp.persist.impl.EntityPersistenceImpl;


public abstract class JmpPersistenceManager {
    public static IJmpPersistence getInstance() throws JmpDbException {
        return new EntityPersistenceImpl();
    }
    
    public static IJmpPersistence getInstance(String dsName) throws JmpDbException {
        return new EntityPersistenceImpl(dsName);
    }
}