package net.juniper.jmp.persist.impl;

import javax.persistence.Entity;

import net.juniper.jmp.persist.exp.JmpDbRuntimeException;

/**
 * Mapping meta from JPA Entity to Db table
 * @author juntaod
 *
 */
public final class JpaEntityMappingMeta extends MappingMeta<Object> {
	private static final long serialVersionUID = 8015429393959008969L;
	public JpaEntityMappingMeta(Class<Object> clazz) {
		super(clazz);
	}

	@Override
	public void resolve(Class<Object> clazz) {
		Entity entity = clazz.getAnnotation(Entity.class);
		if(entity == null)
			throw new JmpDbRuntimeException("This class is not a jpa entity," + clazz.getName());
		String tableName = detectTableName(clazz, entity);
		String pkField = clazz.getFields()[0].getAnnotation(annotationClass);
	}

	private String detectTableName(Class<Object> clazz, Entity entity) {
		return null;
	}
}
