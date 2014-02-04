package net.juniper.jmp.utils;

import java.util.List;

import net.juniper.jmp.wtf.ctx.Page;

public interface IMoEntityConvertor<T, K> {
	public List<K> convertFromMo2Entity(List<T> moList, Class<K> clazz);
	
	public List<T> convertFromEntity2Mo(List<K> entityList, Class<T> clazz);
	
	public Page<T> convertFromEntity2Mo(Page<K> entityList, Class<T> clazz);

	public K convertFromMo2Entity(T server, Class<K> clazz);

	public T convertFromEntity2Mo(K result, Class<T> clazz);
}
