package net.juniper.space.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class MoEntityConvertor<T, K> implements IMoEntityConvertor<T, K>{
	private Logger log = LoggerFactory.getLogger(MoEntityConvertor.class);
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
				BeanUtils.copyProperties(mo, entity);
			}
			return result;
		}
		catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
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
				BeanUtils.copyProperties(entity, mo);
			}
			return result;
		}
		catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Page<T> convertFromEntity2Mo(Page<K> entityPage, Class<T> clazz) {
		if(entityPage == null)
			return null;
		try {
			List<T> content = convertFromEntity2Mo(entityPage.getContent(), clazz);
			//had to find it using reflect
			Field f = entityPage.getClass().getField("pageable");
			Pageable pageable = (Pageable) f.get(entityPage);
			Page<T> result = new PageImpl<T>(content, pageable, entityPage.getTotalElements());
			return result;
		}
		catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public K convertFromMo2Entity(T mo, Class<K> clazz) {
		if(mo == null)
			return null;
		try {
			K entity = clazz.newInstance();
			BeanUtils.copyProperties(entity, mo);
			return entity;
		} 
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public T convertFromEntity2Mo(K entity, Class<T> clazz) {
		if(entity == null)
			return null;
		try {
			T mo = clazz.newInstance();
			BeanUtils.copyProperties(mo, entity);
			return mo;
		} 
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
