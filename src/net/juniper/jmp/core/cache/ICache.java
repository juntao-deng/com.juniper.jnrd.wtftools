package net.juniper.jmp.core.cache;

public interface ICache {
	public void addCache(String key, Object value);
	public Object getCache(String key);
	public void clearCache();
}
