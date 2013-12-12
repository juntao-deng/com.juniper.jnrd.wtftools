package net.juniper.jmp.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCacheImpl implements ICache {
	private Map<String, Object> concurrentMap = new ConcurrentHashMap<String, Object>();
	@Override
	public void addCache(String key, Object value) {
		this.concurrentMap.put(key, value);
	}

	@Override
	public Object getCache(String key) {
		return this.concurrentMap.get(key);
	}

	@Override
	public void clearCache() {
		this.concurrentMap.clear();
	}

}
