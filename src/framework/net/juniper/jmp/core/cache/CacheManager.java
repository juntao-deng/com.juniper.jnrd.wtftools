package net.juniper.jmp.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CacheManager {
	private Map<String, ICache> cacheMap = new ConcurrentHashMap<String, ICache>();
	private static CacheManager instance = new CacheManager();
	private CacheManager() {
		
	}
	
	public static CacheManager getInstance() {
		return instance;
	}
	public ICache getStrongCache(String cacheId){
		ICache cache = cacheMap.get(cacheId);
		if(cache == null){
			synchronized(cacheMap){
				cache = cacheMap.get(cacheId);
				if(cache == null){
					cache = new SimpleCacheImpl();
					cacheMap.put(cacheId, cache);
				}
			}
		}
		return cache;
	}
}
