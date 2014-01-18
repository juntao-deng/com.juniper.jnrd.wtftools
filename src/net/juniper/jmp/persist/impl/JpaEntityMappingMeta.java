package net.juniper.jmp.persist.impl;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.juniper.jmp.persist.exp.JmpDbRuntimeException;

/**
 * Mapping meta from JPA Entity to Db table
 * @author juntaod
 *
 */
public final class JpaEntityMappingMeta extends MappingMeta<Object> {
	private static final long serialVersionUID = 8015429393959008969L;
	public JpaEntityMappingMeta(String dataSource, Class<Object> clazz) {
		super(dataSource, clazz);
	}

	@Override
	public void resolve(String dataSource, Class<Object> clazz) {
		Entity entity = clazz.getAnnotation(Entity.class);
		if(entity == null)
			throw new JmpDbRuntimeException("This class is not a jpa entity," + clazz.getName());
		String tableName = detectTableName(clazz);
//		String pkField = clazz.getFields()[0].getAnnotation(annotationClass);
	}

	/**
	 * try to find db table name
	 * @param clazz
	 * @return
	 */
	private String detectTableName(Class<Object> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if(table != null){
			return table.name();
		}
		Entity entity = clazz.getAnnotation(Entity.class);
		String name = entity.name();
		if(name != null && !name.equals(""))
			return name;
		return clazz.getSimpleName();
	}
}
