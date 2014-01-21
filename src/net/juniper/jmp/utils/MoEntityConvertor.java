package net.juniper.jmp.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.juniper.jmp.core.ctx.Page;
import net.juniper.jmp.core.util.BeanUtils;

import org.apache.log4j.Logger;

public class MoEntityConvertor<T, K> implements IMoEntityConvertor<T, K>{
	private Logger logger = Logger.getLogger(MoEntityConvertor.class);
	
	@Override
	public List<K> convertFromMo2Entity(List<T> moList, Class<K> clazz){
		if(moList == null)
			return null;
		try {
			List<K> result = new ArrayList<K>();
			Iterator<T> it = moList.iterator();
			while(it.hasNext()){
				T mo = it.next();
				K entity = clazz.newInstance();
				result.add(entity);
				BeanUtils.copyPropertiesFromMoToEntity(mo, entity);
			}
			return result;
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@Override
	public List<T> convertFromEntity2Mo(List<K> entityList, Class<T> clazz){
		if(entityList == null)
			return null;
		try {
			List<T> result = new ArrayList<T>();
			Iterator<K> it = entityList.iterator();
			while(it.hasNext()){
				K entity = it.next();
				T mo = clazz.newInstance();
				result.add(mo);
				BeanUtils.copyPropertiesFromEntityToMo(entity, mo);
			}
			return result;
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Page<T> convertFromEntity2Mo(Page<K> entityPage, Class<T> clazz) {
		if(entityPage == null)
			return null;
		try {
			List<T> content = convertFromEntity2Mo(entityPage.getRecords(), clazz);
			Page<T> result = new Page<T>(content, entityPage.getPageIndex(), entityPage.getPageSize(), entityPage.getTotalRecords());
			return result;
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public K convertFromMo2Entity(T mo, Class<K> clazz) {
		if(mo == null)
			return null;
		try {
			K entity = clazz.newInstance();
			BeanUtils.copyPropertiesFromMoToEntity(mo, entity);
			return entity;
		} 
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public T convertFromEntity2Mo(K entity, Class<T> clazz) {
		if(entity == null)
			return null;
		try {
			T mo = clazz.newInstance();
			BeanUtils.copyPropertiesFromEntityToMo(entity, mo);
			return mo;
		} 
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
